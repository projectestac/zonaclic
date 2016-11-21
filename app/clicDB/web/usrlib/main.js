/* global $, gapi */

function onSignIn(googleUser) {
  // Useful data for your client-side scripts:
  var profile = googleUser.getBasicProfile();
  console.log("ID: " + profile.getId()); // Don't send this directly to your server!
  console.log('Full Name: ' + profile.getName());
  console.log('Given Name: ' + profile.getGivenName());
  console.log('Family Name: ' + profile.getFamilyName());
  console.log("Image URL: " + profile.getImageUrl());
  console.log("Email: " + profile.getEmail());

  // The ID token you need to pass to your backend:
  var id_token = googleUser.getAuthResponse().id_token;
  console.log("ID Token: " + id_token);

  $('#result').html('<img src="' + profile.getImageUrl() + '">' +
          '<p>ID: ' + profile.getId() + '<br>' +
          'Full Name: ' + profile.getName() + '<br>' +
          'Given Name: ' + profile.getGivenName() + '<br>' +
          'Family Name: ' + profile.getFamilyName() + '<br>' +
          'Image URL: ' + profile.getImageUrl() + '<br>' +
          'Email: ' + profile.getEmail() + '</p>');
  checkIfSignedIn();

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
  if (gapi.auth2.getAuthInstance().isSignedIn.get()) {
    $('#logout').removeClass('hidden');
  } else {
    $('#logout').addClass('hidden');
    $('#result').html('');
  }
}

$(function () {
  checkIfSignedIn();
});

