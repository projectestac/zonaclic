/* global $, gapi, clipboard, dialogPolyfill */

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
  $.post('/db/getUserInfo', { id_token: googleUser.getAuthResponse().id_token }, null, 'json')
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
  var usrlib = 'https://clic.xtec.cat/users?' + data.id;
  $('#userLibUrl').attr({ href: usrlib }).html(usrlib);
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
    $('#userIdBox').removeClass('hidden');
    $('#loginBox').addClass('hidden');
    $('#mainInfo').removeClass('hidden');
  } else {
    // No valid user signed in
    $('#userIdBox').unbind('click');
    $('#userIdBox').addClass('hidden');
    $('.project').remove();
    $('#mainInfo').addClass('hidden');
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
      projects.splice(projects.indexOf(prj), 1);
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
      else
        $fileWarn.empty();

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
      xhr: function () {  // Try to track upload progress with a customized XHR object
        var myXhr = $.ajaxSettings.xhr();
        if (myXhr.upload) { // Check if upload property exists
          myXhr.upload.addEventListener('progress', function (e) {
            if (e.lengthComputable)
              $('#upProgress').attr({ value: e.loaded, max: e.total });
          }, false);
        }
        return myXhr;
      },
      success: function (data) {
        $uploadDlg[0].close();
        addProject(data.project);
      },
      error: function (xhr, status) {
        $('#uploadMsg').html('S\'ha produït un error: ' + xhr.statusText + ' ' + status);
        $('#upProgress').addClass('hidden');
      },
      data: formData,
      cache: false,
      contentType: false,
      processData: false
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
  $('#scormFileInput, #projectNameInput').attr({ value: '' });
  $('#fileNameInfo, #fileWarn, #fileNameWarn').empty();
  $('#fileInfoBlock, #folderNameBlock, #fileUploadProgressBlock, #upProgress').addClass('hidden');
  $('#uploadForm').removeClass('hidden');
  $('#uploadOK').prop('disabled', true);
  $('#uploadCancel').prop('disabled', false);
  // Open the dialog
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
        data: { project: project.name },
        success: function (e) {
          if (e.status === 'ok') {
            removeProject(project);
            $deleteDlg.data('project', null);
            $deleteDlg[0].close();
          } else {
            $('#deleteMsg').html('Error inesperat: ' + e.status + ' - ' + e.err).removeClass('hidden');
          }
        },
        error: function (xhr, status) {
          $('#deleteMsg').html('ERROR ' + xhr.statusText + ' ' + status).removeClass('hidden');
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

// Initialize the 'share project' dialog
function initShareDlg() {
  var $shareDlg = $('#shareDlg');
  dialogPolyfill.registerDialog($shareDlg[0]);
  $('.shareText')
    .on('focus', function () { this.setSelectionRange(0, this.value.length); })
    .attr('spellcheck', false);
  $('#linkTextCopy').on('click', function () {
    clipboard.copy($('#directLink').text());
    $('#copy-toast')[0].MaterialSnackbar.showSnackbar({ message: 'L\'enllaç s\'ha copiat al porta-retalls' });
  });
  $('#embedCodeCopy').on('click', function () {
    clipboard.copy($('#embedCode').text());
    $('#copy-toast')[0].MaterialSnackbar.showSnackbar({ message: 'El codi s\'ha copiat al porta-retalls. Enganxeu-lo amb CTRL+V a la pàgina o article del blog on vulgueu que aparegui.' });
  });
  $('#moodleLinkCopy').on('click', function () {
    clipboard.copy($('#moodleLink').text());
    $('#copy-toast')[0].MaterialSnackbar.showSnackbar({ message: 'L\'enllaç s\'ha copiat al porta-retalls. Enganxeu-lo amb CTRL+V en una activitat de tipus JClic de Moodle.' });
  });
  $('#closeShareDlg').on('click', function () {
    shareDlg.close();
  });
}

// Open the 'share project' dialog
function openShareDlg(project) {

  $('#shareDlgTitle').html(getQuotedText(project.title));

  var basePath = usrLibRoot + project.basePath + '/';
  var directLink = basePath + 'index.html';
  var moodleLink = basePath + project.mainFile;
  var shareText = encodeURIComponent('Activitats JClic ' + getQuotedText(project.title) + ' ' + directLink);
  $('#directLink').val(directLink);
  $('#shTwitter').attr('href', 'https://twitter.com/intent/tweet?' +
    'text=' + encodeURIComponent(getQuotedText(project.title)) +
    '&url=' + encodeURIComponent(directLink) +
    '&hashtags=JClic' +
    '&via=jclic');

  //$('#shFacebook').attr('href', 'https://www.facebook.com/sharer/sharer.php?u=' + encodeURIComponent(directLink));
  $('#shFacebook').attr('href', 'https://www.facebook.com/dialog/feed?' +
    'app_id=1917838468440790' +
    '&picture=' + encodeURIComponent(basePath + project.cover) +
    '&name=' + encodeURIComponent(project.title) +
    '&description=' + encodeURIComponent('Proveu aquestes activitats amb el nou JClic per a HTML5') +
    '&redirect_uri=' + encodeURIComponent('http://facebook.com/') +
    '&link=' + encodeURIComponent(directLink));

  $('#shGoogle').attr('href', 'https://plus.google.com/share?url=' + encodeURIComponent(directLink));

  $('#shPinterest').attr('href',
    'https://pinterest.com/pin/create/button/?url=' + encodeURIComponent(directLink) +
    '&media=' + encodeURIComponent(basePath + project.cover) +
    '&description=' + encodeURIComponent(project.title));

  $('#shEmail').attr('href', 'mailto:?' +
    'subject=' + encodeURIComponent('Activitats JClic ' + getQuotedText(project.title)) +
    '&body=' + encodeURIComponent(project.title + '\n\nProveu aquestes activitats amb el nou JClic per a HTML5:\n' + directLink)
  );

  $('#embedCode').val(getEmbedCode(directLink));
  $('#moodleLink').val(moodleLink);

  $('#shareDlg')[0].showModal();
}

// Build a card with information and action buttons related to the given project
function $buildProjectCard(project) {
  var basePath = usrLibRoot + project.basePath + '/';
  var $result = $('<div/>', { class: 'project mdl-cell mdl-card mdl-shadow--2dp' }).data('project', project);

  $result.append($('<div/>', { class: 'mdl-card__title' }).css({ background: 'url(\'' + basePath + project.cover + '\') center / cover' })
    .append($('<h2/>', { class: 'mdl-card__title-text' }).html(project.title)));

  var lang = project.meta_langs && project.meta_langs.length > 0 ? project.meta_langs[0] : '';

  var $cardBody = $('<div/>', { class: 'mdl-card__supporting-text' }).append(
    $('<p/>', { class: 'prjAuthor' }).html(project.author),
    $('<p/>', { class: 'prjSchool' }).html(project.school));

  var $tBody = $('<tbody/>').append(
    $('<tr/>').append($('<td>').html('Directori:'), $('<td>', { class: 'code' }).html(project.name)),
    $('<tr/>').append($('<td>').html('Mida:'), $('<td>').html(toMB(project.totalFileSize) + ' MB')),
    $('<tr/>').append($('<td>').html('Data:'), $('<td>').html(project.date)));

  if (project.languages && project.languages[lang])
    $tBody.append($('<tr/>').append($('<td>').html('Idiomes:'), $('<td>').html(project.languages[lang])));

  if (project.levels && project.levels[lang])
    $tBody.append($('<tr/>').append($('<td>').html('Nivells:'), $('<td>').html(project.levels[lang])));

  if (project.areas && project.areas[lang])
    $tBody.append($('<tr/>').append($('<td>').html('Àrees:'), $('<td>').html(project.areas[lang])));

  $cardBody.append($('<table/>', { class: 'prjData' }).append($tBody));

  if (project.description && project.description[lang])
    $cardBody.append($('<p/>', { class: 'prjDesc' }).html(project.description[lang]));

  $result.append($cardBody);
  project.card = $result;

  // Create action buttons:
  var $deleteBtn = $('<button/>', { class: 'mdl-button mdl-button--icon mdl-button--colored mdl-js-button mdl-js-ripple-effect', title: 'Esborra el projecte' })
    .append($('<i/>', { class: 'material-icons' }).html('delete'))
    .on('click', function () {
      deleteProject(project);
    });

  var $shareBtn = $('<button/>', {
    id: 'share',
    class: 'mdl-button mdl-button--icon mdl-button--colored mdl-js-button mdl-js-ripple-effect',
    title: 'Comparteix...'
  })
    .append($('<i/>', { class: 'material-icons' }).html('share'))
    .on('click', function () {
      openShareDlg(project);
    });

  var $downloadBtn = $('<a/>', {
    class: 'mdl-button mdl-button--icon mdl-button--colored mdl-js-button mdl-js-ripple-effect',
    title: 'Descarrega el fitxer',
    download: true,
    href: '/db/downloadUserProject?prj=' + project.basePath
  })
    .append($('<i/>', { class: 'material-icons' }).html('cloud_download'));

  /*
  var $editBtn = $('<button/>', {
    class: 'mdl-button mdl-button--icon mdl-button--colored mdl-js-button mdl-js-ripple-effect',
    disabled: true,
    title: 'Edita el projecte'
  })
    .append($('<i/>', { class: 'material-icons' }).html('edit').on('click', function () {
      // TODO: Implement edit
    }));
  */

  var $playBtn = $('<a/>', {
    class: 'mdl-button mdl-button--icon mdl-button--raised mdl-button--accent mdl-js-button mdl-js-ripple-effect',
    title: 'Obre el projecte',
    href: basePath + 'index.html',
    target: '_BLANK'
  })
    .append($('<i/>', { class: 'material-icons' }).html('play_arrow'));

  // Build card
  $result.append($('<div/>', { class: 'mdl-card__actions mdl-card--border' })
    .append($downloadBtn, /* $editBtn, */ $deleteBtn, $shareBtn));

  $result.append($('<div/>', { class: 'mdl-card__menu' })
    .append($playBtn));

  return $result;
}

// Initialization process

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

// Called at startup, when both DOM and Google API methods are ready
function init() {
  initialized = true;
  initUploadDlg();
  initDeleteDlg();
  initShareDlg();
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

// Miscellaneous functions

// Get the given amount of bytes in megabyte units
function toMB(bytes) {
  return Math.round(10 * bytes / (1024 * 1024)) / 10;
}

// Get the HTML code suitable for embedding a JClic project
function getEmbedCode(url, width, height) {
  return '<iframe width="%w%" height="%h%" frameborder="0" allowFullScreen="true" src="%url%"></iframe>'
    .replace('%url%', url)
    .replace('%w%', width ? width : '800')
    .replace('%h%', height ? height : '600');
}

// Surround text with double quotes only if there is no double quote on it
function getQuotedText(text) {
  text = text || '';
  return text.indexOf('"') < 0 ? '"' + text + '"' : text;
}
