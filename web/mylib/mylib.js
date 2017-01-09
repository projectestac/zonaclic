/* global $, gapi */

// Set the location of "users" dir
var url = new URL(window.location.href);
var usrLibRoot = url.protocol + '//' + url.host + '/users/';

// Flags to check when DOM and Google API are ready to start
var gAPI_ready = false, DOM_ready = false, initialized = false;

// Max disk space assigned to the current user, and amount currently used
var userQuota = 0, usedBytes = 0;

// Projects published by the current user
var projects = [];

// Polyfill for String.endsWith
// See: https://developer.mozilla.org/ca/docs/Web/JavaScript/Referencia/Objectes_globals/String/endsWith
if (!String.prototype.endsWith) {
  String.prototype.endsWith = function (searchString, position) {
    var subjectString = this.toString();
    if (typeof position !== 'number' || !isFinite(position) || Math.floor(position) !== position || position > subjectString.length) {
      position = subjectString.length;
    }
    position -= searchString.length;
    var lastIndex = subjectString.indexOf(searchString, position);
    return lastIndex !== -1 && lastIndex === position;
  };
}

// Called when user has been authenticated on Google API
function onSignIn(googleUser) {
  // Let's check if current Google user has XTEC permissions
  $('#gSignInBtn').addClass('hidden');
  $('#loginSpinner').removeClass('hidden');
  var errMsg = null;
  var userData = null;
  $.post('/db/getUserInfo', {id_token: googleUser.getAuthResponse().id_token}, null, 'json')
          .done(function (data) {
            if (data === null || typeof data !== 'object')
              errMsg = 'ERROR: No s\'ha pogut validar l\'usuari. Proveu-ho més tard.';
            else if (data.status !== 'validated')
              errMsg = 'ERROR: ' + data.error;
            else
              userData = data;
          })
          .fail(function (jqXHR, textStatus, errorThrown) {
            errMsg = 'ERROR: ' + textStatus + ' - ' + errorThrown;
            console.log(errorThrown);
            console.log(jqXHR);
          })
          .always(function () {
            if (errMsg) {
              signOut();
              $('#loginMsg').html(errMsg);
            } else if (userData !== null)
              loginOK(userData);
          });

}

// Called when a valid user is logged in
function loginOK(data) {
  $('#fullUserName').html(data.fullUserName);
  $('.avatar').attr('src', data.avatar);
  $('#userName').html(data.id);
  projects = data.projects;
  userQuota = data.quota;
  usedBytes = data.currentSize;
  updateSpaceInfo();
  checkIfSignedIn();

  for (var p = 0; p < projects.length; p++)
    $('#projects').append($buildProjectCard(projects[p]));

  console.log('User ' + data.id + ' signed in.');
}

// Update information about currently used and available space
function updateSpaceInfo() {
  $('#userNumProjects').html(projects.length);
  usedBytes = 0;
  for (var i = 0; i < projects.length; i++)
    usedBytes += projects[i].totalFileSize;
  $('#usedBytes').html(toMB(usedBytes) + ' MB');
  $('#quota').html(toMB(userQuota) + ' MB');
}

// Called on Google log-in errors
function onSignInFailure() {
  $('#loginMsg').html('S\'ha produït un error en validar l\'usuari');
}

// Method used to sign out
function signOut() {
  $('#loginMsg').empty();
  userQuota = 0;
  usedBytes = 0;
  var auth2 = gapi.auth2.getAuthInstance();
  auth2.signOut().then(function () {
    checkIfSignedIn();
    console.log('User signed out.');
  });
}

// Check if a valid user is signed in, and show/hide DOM elements accordingly
function checkIfSignedIn() {
  if (gapi && gapi.auth2 && gapi.auth2.getAuthInstance().isSignedIn.get()) {
    // User is signed in
    $('#accountInfo').removeClass('hidden');
    $('#userIdBox').removeClass('hidden');
    $('#loginBox').addClass('hidden');
  } else {
    // No valid user is signed in
    $('#accountInfo').addClass('hidden');
    $('#userIdBox').addClass('hidden');
    $('.project').remove();
    $('#gSignInBtn').removeClass('hidden');
    $('#loginSpinner').addClass('hidden');
    $('#loginBox').removeClass('hidden');
  }
}

// Find the project with the given name
function findProject(folderName) {
  for (var i = 0; i < projects.length; i++)
    if (projects[i].name === folderName)
      return projects[i];
  return null;
}

// Add a new project to `projects`, removing duplicate if needed
function addProject(project) {
  if (project !== null) {
    removeProject(project);
    projects.push(project);
    $('#projects').append($buildProjectCard(project));
    updateSpaceInfo();
  }
}

// Remove the given project from `projects`
function removeProject(project) {
  if (project !== null) {
    var prj = findProject(project.name);
    if (prj !== null) {
      if (prj.card)
        prj.card.remove();
      var p = projects.indexOf(prj);
      projects = projects.splice(p, 1);
      updateSpaceInfo();
    }
  }
}

// Initialize the 'upload project' dialog
function initUploadDlg() {
  var $uploadDlg = $('#uploadDlg');
  dialogPolyfill.registerDialog($uploadDlg[0]);

  var $folderInput = $('#projectNameInput'),
          $folderWarn = $('#fileNameWarn'),
          $uploadOK = $('#uploadOK'),
          $fileWarn = $('#fileWarn');

  // Check the proposed project name and size
  $('#scormFileInput').on('change', function () {
    if (this.files.length > 0) {
      var file = this.files[0];
      $('#fileNameInfo').html(file.name);
      $('#fileSizeInfo').html('(' + toMB(file.size) + ' MB)');

      var folderName = file.name.substring(0, file.name.indexOf('.')).trim().replace(/[\W]/gi, '_').toLowerCase() || 'prj';
      var sizeOk = file.size < (userQuota - usedBytes);
      var typeOk = file.name.toLowerCase().endsWith('.scorm.zip');

      if (!sizeOk)
        $fileWarn.html('ERROR: El fitxer seleccionat excedeix l\'espai disponible!');
      else if (!typeOk)
        $fileWarn.html('ERROR: El fitxer seleccionat no és del tipus ".scorm.zip"');

      $('#fileInfoBlock').removeClass('hidden');

      if (sizeOk && typeOk) {
        // File ready to upload
        $folderInput.val(folderName).trigger('input');
        $('#folderNameBlock').removeClass('hidden');
        $uploadOK.prop('disabled', false);
      } else {
        // File is not ready for uploading
        $('#folderNameBlock').addClass('hidden');
        $uploadOK.prop('disabled', true);
      }
    } else {
      // Clear dialog
      $('#fileInfo, #fileWarn').html('');
      $('#fileInfoBlock, #folderNameBlock').addClass('hidden');
      $uploadOK.prop('disabled', true);
    }
  });

  // Dynamically check proposed project names
  $folderInput.on('input', function () {
    var folderName = $folderInput.val();
    var ok = false;
    if (folderName === '')
      $folderWarn.html('ERROR: Heu d\'especificar un nom per al directori on es publicarà el projecte.');
    else if (findProject(folderName) !== null) {
      $folderWarn.html('ATENCIÓ: Ja existeix un projecte en aquest directori. El seu contingut serà substituït.<br>Feu-ho només si el que preteneu és publicar una nova versió del mateix projecte.');
      ok = true;
    } else if (/[\W]/gi.test(folderName))
      $folderWarn.html('ERROR: El nom del directori no pot contenir accents, espais ni caràcters especials.');
    else {
      $folderWarn.html('');
      ok = true;
    }
    $uploadOK.prop('disabled', !ok);
  });

  // Upload the file
  $uploadOK.on('click', function () {
    $uploadOK.prop('disabled', true);
    $('#uploadForm').addClass('hidden');
    $('#uploadMsg').html('S\'està pujant el fitxer...');
    // TODO: display file name
    $('#fileUploadProgressBlock').removeClass('hidden');

    var formData = new FormData($('#uploadForm')[0]);
    $.ajax({
      url: '/db/uploadUserFile',
      type: 'POST',
      xhr: function () {  // Custom XMLHttpRequest
        var myXhr = $.ajaxSettings.xhr();
        if (myXhr.upload) { // Check if upload property exists
          myXhr.upload.addEventListener('progress', function (e) {
            if (e.lengthComputable)
              $('#upProgress').attr({value: e.loaded, max: e.total});
          }, false);
        }
        return myXhr;
      },
      //Ajax events
      //beforeSend: beforeSendHandler,
      success: function (data) {
        $uploadDlg[0].close();
        addProject(data.project);
      },
      error: function (data) {
        var err = data.error || 'Error desconegut!';
        $('#uploadMsg').html('S\'ha produït un error en pujar el fitxer: ' + err);
        $('#upProgress').addClass('hidden');
      },
      data: formData,
      cache: false,
      contentType: false,
      processData: false
              //dataType: 'json'
    });

    // Display progress bar
    $('#upProgress').removeClass('hidden');
  });

  $('#uploadCancel').on('click', function () {
    $uploadDlg[0].close();
  });
}

// Prepare and open the upload dialog
function uploadProject() {
  // Clean existing data
  $('#uploadForm')[0].reset();
  $('#scormFileInput, #projectNameInput').attr({value: ''});
  $('#fileNameInfo, #fileWarn, #fileNameWarn').empty();
  $('#fileInfoBlock, #folderNameBlock, #fileUploadProgressBlock, #upProgress').addClass('hidden');
  $('#uploadForm').removeClass('hidden');
  $('#uploadOK').prop('disabled', true);
  $('#uploadCancel').prop('disabled', false);
  // Open dialog
  $('#uploadDlg')[0].showModal();
}

// Initialize the 'delete project' dialog
function initDeleteDlg() {
  var $deleteDlg = $('#confirmDeleteDlg');
  dialogPolyfill.registerDialog($deleteDlg[0]);

  $('#deleteCancel').on('click', function () {
    $deleteDlg[0].close();
  });

  $('#deleteOK').on('click', function () {
    var project = $deleteDlg.data('project');
    if (project) {
      $('#deleteOK').prop('disabled', true);
      $.ajax({
        url: '/db/deleteProject',
        type: 'POST',
        data: {project: project.name},
        success: function (e) {
          if (e.status === 'ok') {
            removeProject(project);
            $deleteDlg.data('project', null);
            $deleteDlg[0].close();
          } else {
            $('#deleteMsg').html('Error inesperat: ' + e.status + ' - ' + e.err).removeClass('hidden');
          }
        },
        error: function (xhr, err) {
          // TODO: Improve the info displayed about the error
          $('#deleteMsg').html('ERROR ' + xhr.status + ' ' + xhr.responseText).removeClass('hidden');
        },
        cache: false,
        dataType: 'json'
      });
    }
  });
}

// Prepare and open the delete dialog
function deleteProject(project) {
  // Load dialog project
  $('#confirmDeleteDlg').data('project', project);
  $('.deleteName').html(project.name);
  // Clean existing message
  $('#deleteMsg').html('').addClass('hidden');
  // Enable OK button
  $('#deleteOK').prop('disabled', false);
  // Open dialog
  $('#confirmDeleteDlg')[0].showModal();
}


// Called when DOM is fully initialized
$(function () {
  DOM_ready = true;
  if (gAPI_ready && !initialized)
    init();
});

// Called when Google API is ready
function gApiLoaded() {
  gAPI_ready = true;
  if (DOM_ready && !initialized)
    init();
}

// Called at startup, when both DOM and Google API are ready
function init() {
  initialized = true;
  initUploadDlg();
  initDeleteDlg();
  checkIfSignedIn();
  $('#uploadBtn').on('click', uploadProject);
  $('#logoutBtn').on('click', signOut);
  gapi.signin2.render('gSignInBtn', {
    'scope': 'profile email',
    'width': 240,
    'height': 50,
    'longtitle': false,
    'theme': 'dark',
    'onsuccess': onSignIn,
    'onfailure': onSignInFailure
  });
}

// Build a card with information and action buttons related to the given project
function $buildProjectCard(project) {
  var basePath = usrLibRoot + project.basePath + '/';
  var $result = $('<div/>', {class: 'project mdl-cell mdl-card mdl-shadow--2dp'}).data('project', project);

  $result.append($('<div/>', {class: 'mdl-card__title'}).css({background: 'url(\'' + basePath + project.cover + '\') center / cover'})
          .append($('<h2/>', {class: 'mdl-card__title-text'}).html(project.title)));

  var lang = project.meta_langs && project.meta_langs.length > 0 ? project.meta_langs[0] : '';

  $result.append($('<div/>', {class: 'mdl-card__supporting-text'}).html(
          'Carpeta: ' + project.name + '<br>\n' +
          'Mida: ' + toMB(project.totalFileSize) + ' MB<br>\n' +
          'Autor/a: ' + project.author + '<br>\n' +
          'Centre: ' + project.school + '<br>\n' +
          (project.languages && project.languages[lang] ? 'Idioma: ' + project.languages[lang] + '<br>\n' : '') +
          (project.levels && project.levels[lang] ? 'Nivell: ' + project.levels[lang] + '<br>\n' : '') +
          (project.areas && project.areas[lang] ? 'Àrea: ' + project.areas[lang] + '<br>\n' : '') +
          (project.descriptions && project.descriptions[lang] ? project.descriptions[lang] : '')
          ));

  project.card = $result;

  // Create action buttons:

  var $deleteBtn = $('<button/>', {class: 'mdl-button mdl-button--icon mdl-button--colored mdl-js-button mdl-js-ripple-effect', title: 'Esborra el projecte'})
          .append($('<i/>', {class: 'material-icons'}).html('delete').on('click', function () {
            deleteProject(project);
          }));

  var $shareBtn = $('<button/>', {
    class: 'mdl-button mdl-button--icon mdl-button--colored mdl-js-button mdl-js-ripple-effect',
    disabled: true,
    title: 'Comparteix...'})
          .append($('<i/>', {class: 'material-icons'}).html('share').on('click', function () {
            // TODO: Implement share options
          }));

  var $downloadBtn = $('<a/>', {
    class: 'mdl-button mdl-button--icon mdl-button--colored mdl-js-button mdl-js-ripple-effect',
    title: 'Descarrega el fitxer',
    href: '/db/downloadUserProject?prj=' + project.basePath})
          .append($('<i/>', {class: 'material-icons'}).html('cloud_download'));

  var $editBtn = $('<button/>', {
    class: 'mdl-button mdl-button--icon mdl-button--colored mdl-js-button mdl-js-ripple-effect',
    disabled: true,
    title: 'Edita el projecte'})
          .append($('<i/>', {class: 'material-icons'}).html('edit').on('click', function () {
            // TODO: Implement edit
          }));

  var $playBtn = $('<a/>', {
    class: 'mdl-button mdl-button--icon mdl-button--raised mdl-button--accent mdl-js-button mdl-js-ripple-effect',
    title: 'Obre el projecte',
    href: basePath + 'index.html',
    target: '_BLANK'})
          .append($('<i/>', {class: 'material-icons'}).html('play_arrow'));

  // Build card
  $result.append($('<div/>', {class: 'mdl-card__actions mdl-card--border'})
          .append($downloadBtn, $editBtn, $deleteBtn, $shareBtn));

  $result.append($('<div/>', {class: 'mdl-card__menu'})
          .append($playBtn));

  return $result;
}

// Miscellaneous functions

// Express the given amount of bytes in megabyte units
function toMB(bytes) {
  return Math.round(10 * bytes / (1024 * 1024)) / 10;
}
