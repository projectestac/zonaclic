/* global $, gapi */

// St the location of "users" dir
var url = new URL(window.location.href);
var usrLibRoot = url.protocol + '//' + url.host + '/users/';

// Flags to check when DOM and Google API are ready to start
var gAPI_ready = false, DOM_ready = false, initialized = false;

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
  $('#availBytes').html(toMB(data.quota - data.currentSize) + ' MB');
  
  checkIfSignedIn();
  console.log('User ' + data.id + ' signed in.');
}

// Called on Google log-in errors
function onSignInFailure() {
  $('#loginMsg').html('S\'ha produït un error en validar l\'usuari');
}

// Method used to sign out of the app.
function signOut() {
  $('#loginMsg').empty();
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

// Called when DOM is fully initialized
$(function () {
  console.log('DOM ready');
  DOM_ready = true;
  if (gAPI_ready && !initialized)
    init();
});

// Called when Google API is ready
function gApiLoaded() {
  console.log('gAPI loaded');
  gAPI_ready = true;
  if (DOM_ready && !initialized)
    init();
}

// Called at startup, when DOM and Google API are ready
function init() {
  initialized = true;
  checkIfSignedIn();
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

function toMB(bytes) {
  return Math.round(10*bytes/(1024*1024))/10;
}
