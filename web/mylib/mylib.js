/* global $, gapi */

// St the location of "users" dir
var url = new URL(window.location.href);
var usrLibRoot = url.protocol + '//' + url.host + '/users/';

// Flags to check when DOM and Google API are ready to start
var gAPI_ready = false, DOM_ready = false, initialized = false;

var currentAvailSize = 0;

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

// Called when we have a valid XTEC user
function loginOK(data) {
  $('#fullUserName').html(data.fullUserName);
  $('.avatar').attr('src', data.avatar);
  $('#userName').html(data.id);
  $('#userNumProjects').html(data.projects.length);
  $('#usedBytes').html(toMB(data.currentSize) + ' MB');
  $('#quota').html(toMB(data.quota) + ' MB');
  currentAvailSize = data.quota - data.currentSize;

  checkIfSignedIn();

  for (var p = 0; p < data.projects.length; p++)
    $('#mainGrid').append($buildProjectCard(data.projects[p]));

  console.log('User ' + data.id + ' signed in.');
}

// Called on Google log-in errors
function onSignInFailure() {
  $('#loginMsg').html('S\'ha produït un error en validar l\'usuari');
}

// Method used to sign out of the app.
function signOut() {
  $('#loginMsg').empty();
  currentAvailSize = 0;
  var auth2 = gapi.auth2.getAuthInstance();
  auth2.signOut().then(function () {
    checkIfSignedIn();
    console.log('User signed out.');
  });
}

// Checks if a valid user is signed in, and shows/hides DOM elements accordingly
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

function initUploadDlg() {

  var $uploadDlg = $('#uploadDlg');
  dialogPolyfill.registerDialog($uploadDlg[0]);

  // Check proposed file name and size
  $('#scormFileInput').on('change', function () {
    var file = this.files[0];
    var folderName = file.name.substring(0, file.name.indexOf('.'));
    $('#fileInfo').html(file.name + ' (' + toMB(file.size) + ' MB)');

    var sizeOk = file.size < currentAvailSize;
    var typeOk = file.name.toLowerCase().endsWith('.scorm.zip');

    if (!sizeOk)
      $('#fileWarn').html('ERROR: El fitxer seleccionat excedeix l\'espai disponible!');
    else if (!typeOk)
      $('#fileWarn').html('ERROR: El fitxer seleccionat no és del tipus ".scorm.zip"');

    $('#fileInfoBlock').removeClass('hidden');

    if (sizeOk && typeOk) {
      $('#projectNameInput').attr({value: folderName});
      // Todo: check for duplicate folder names!
      $('#folderNameBlock').removeClass('hidden');
      $('#uploadOK').prop('disabled', false);
    } else {
      $('#folderNameBlock').addClass('hidden');
      $('#uploadOK').prop('disabled', true);
    }
  });

  // Upload the file
  $('#uploadOK').on('click', function () {
    
    $('#uploadOK').prop('disabled', true);
    $('#uploadForm').addClass('hidden');
    $('#uploadMsg').html('S\'està pujant el fitxer...');
    // TODO: display file name
    $('#spinner').addClass('is-active');
    $('#fileUploadProgressBlock').removeClass('hidden');

    var formData = new FormData($('#uploadForm')[0]);
    $.ajax({
      url: '/db/uploadUserFile',
      type: 'POST',
      xhr: function () {  // Custom XMLHttpRequest
        var myXhr = $.ajaxSettings.xhr();        
        if (myXhr.upload) { // Check if upload property exists
          // TODO: Add progress handling function
          // myXhr.upload.addEventListener('progress', progressHandlingFunction, false); // For handling the progress of the upload
        }
        return myXhr;
      },
      //Ajax events
      //beforeSend: beforeSendHandler,
      success: function(data){
        $uploadDlg[0].close();        
        if (data.project){
          // TODO: Remove possible duplicates!
          $('#mainGrid').append($buildProjectCard(data.project));
        }
      },
      error: function(data){
        var err = data.error || 'Error desconegut!';
        $('#uploadMsg').html('S\'ha produït un error: ' + err);
        $('#spinner').removeClass('is-active');        
      },
      // Form data
      data: formData,
      //Options to tell jQuery not to process data or worry about content-type.
      cache: false,
      contentType: false,
      processData: false
      //dataType: 'json'
    });
  });

  $('#uploadCancel').on('click', function () {
    $uploadDlg[0].close();
  });
}

function uploadProject() {
  // Clear existing data
  $('#scormFileInput, #projectNameInput').attr({value: ''});
  $('#fileInfo, #fileWarn, #fileNameWarn').empty();
  $('#fileInfoBlock, #folderNameBlock, #fileUploadProgressBlock').addClass('hidden');
  $('#uploadForm').removeClass('hidden');
  $('#uploadOK').prop('disabled', true);
  $('#uploadCancel').prop('disabled', false);
  $('#uploadDlg')[0].showModal();
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

// Called at startup, when DOM and Google API are ready
function init() {
  initialized = true;
  initUploadDlg();
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

function $buildProjectCard(project) {
  var basePath = usrLibRoot + project.basePath + '/';
  var $result = $('<div/>', {class: 'project mdl-cell mdl-cell--6-col project mdl-card mdl-shadow--2dp'});

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

  var $deleteBtn = $('<button/>', {class: 'mdl-button mdl-button--icon mdl-button--colored mdl-js-button mdl-js-ripple-effect', title: 'Esborra el projecte'})
          .append($('<i/>', {class: 'material-icons'}).html('delete')).on('click', function () {
    if (window.confirm('Segur que voleu esborrar definitivament el projecte? (no es pot desfer)'))
      $.ajax({
        url: '/db/deleteProject',
        type: 'POST',
        data: {project: project.name},
        success: function (e) {
          if (e.status === 'ok')
            $result.remove();
          else
            window.alert(e.status + ' ' + e.err);
        },
        error: function (xhr, err) {
          // TODO: Improve the info displayed about the error
          window.alert('ERROR ' + xhr.status + ' ' + xhr.responseText);
        },
        cache: false,
        dataType: 'json'
      });
  });

  var $shareBtn = $('<button/>', {class: 'mdl-button mdl-button--icon mdl-button--colored mdl-js-button mdl-js-ripple-effect', title: 'Comparteix...'})
          .append($('<i/>', {class: 'material-icons'}).html('share').on('click', function () {
            // TODO: Implement share options
          }));

  var $downloadBtn = $('<button/>', {class: 'mdl-button mdl-button--icon mdl-button--colored mdl-js-button mdl-js-ripple-effect', title: 'Descarrega el fitxer'})
          .append($('<i/>', {class: 'material-icons'}).html('cloud_download').on('click', function () {
            // TODO: Implement file download
          }));

  var $editBtn = $('<button/>', {class: 'mdl-button mdl-button--icon mdl-button--colored mdl-js-button mdl-js-ripple-effect', title: 'Edita el projecte'})
          .append($('<i/>', {class: 'material-icons'}).html('edit').on('click', function () {
            // TODO: Implement edit
          }));

  var $playBtn = $('<a/>', {
    class: 'mdl-button mdl-button--icon mdl-button--raised mdl-button--accent mdl-js-button mdl-js-ripple-effect',
    title: 'Obre el projecte',
    href: basePath + 'index.html',
    target: '_BLANK'})
          .append($('<i/>', {class: 'material-icons'}).html('play_arrow'));

  $result.append($('<div/>', {class: 'mdl-card__actions mdl-card--border'})
          .append($downloadBtn, $editBtn, $deleteBtn, $shareBtn));

  $result.append($('<div/>', {class: 'mdl-card__menu'})
          .append($playBtn));

  return $result;
}

// Miscellaneous functions
//
function toMB(bytes) {
  return Math.round(10 * bytes / (1024 * 1024)) / 10;
}
