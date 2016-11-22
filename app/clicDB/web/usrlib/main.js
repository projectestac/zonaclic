/* global $, gapi */

function onSignIn(googleUser) {

  checkIfSignedIn();

  /*
   // Useful data for your client-side scripts:
   var profile = googleUser.getBasicProfile();
   console.log("ID: " + profile.getId()); // Don't send this directly to your server!
   console.log('Full Name: ' + profile.getName());
   console.log('Given Name: ' + profile.getGivenName());
   console.log('Family Name: ' + profile.getFamilyName());
   console.log("Image URL: " + profile.getImageUrl());
   console.log("Email: " + profile.getEmail());
   
   $('#result').html('<img src="' + profile.getImageUrl() + '">' +
   '<p>ID: ' + profile.getId() + '<br>' +
   'Full Name: ' + profile.getName() + '<br>' +
   'Given Name: ' + profile.getGivenName() + '<br>' +
   'Family Name: ' + profile.getFamilyName() + '<br>' +
   'Image URL: ' + profile.getImageUrl() + '<br>' +
   'Email: ' + profile.getEmail() + '</p>');
   
   // The ID token you need to pass to your backend:
   var id_token = googleUser.getAuthResponse().id_token;
   console.log("ID Token: " + id_token);
   */

  $.post('getUserInfo.jsp', {id_token: googleUser.getAuthResponse().id_token})
          .done(function (result) {
            $('#result').empty()
                    .append($('<img/>', {src: result.avatar}))
                    .append($('<ul/>').apend([
                      $('<li/>').html('User: ' + result.user),
                      $('<li/>').html('Email: ' + result.email),
                      $('<li/>').html('Expires: ' + result.expires)
                    ]));
          })
          .fail(function (error) {
            $('#result').empty().append('ERROR: ' + error);
            console.log(error);
          }).always(function () {
    // TODO: Remove waiting img
  });
}

function onFailure() {
  checkIfSignedIn();
  $('#result').html('Login failed!');
}

function signOut() {
  var auth2 = gapi.auth2.getAuthInstance();
  if (auth2)
    auth2.signOut().then(function () {
      console.log('User signed out.');
      checkIfSignedIn();
    });
}

function checkIfSignedIn() {
  if (gapi && gapi.auth2 && gapi.auth2.getAuthInstance().isSignedIn.get()) {
    $('#logout').removeClass('hidden');
  } else {
    $('#logout').addClass('hidden');
    $('#result').empty();
  }
}

// Called when Google API is ready
function start() {
  gapi.signin2.render('signInBtn', {
    'scope': 'profile email',
    'width': 240,
    'height': 50,
    'longtitle': true,
    'theme': 'dark',
    'onsuccess': onSignIn,
    'onfailure': onFailure
  });

  $('#logout').click(signOut);

  checkIfSignedIn();
}

