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

  $('#result').empty();
  
  // TODO: put waiting image

  $.post('/db/getUserInfo.jsp', {id_token: googleUser.getAuthResponse().id_token}, null, 'json')
          .done(function (data) {
            if (data === null || typeof data !== 'object')
              $('#result').html('ERROR: No data received!');
            else if (data.status !== 'validated')
              $('#result').html('ERROR: ' + data.error);
            else {
              $('#result')
                      .append($('<img/>', {src: data.avatar}))
                      .append($('<ul/>').append([
                        $('<li/>').html('User: ' + data.fullUserName),
                        $('<li/>').html('Email: ' + data.email),
                        $('<li/>').html('Id: ' + data.id),
                        $('<li/>').html('Quota: ' + data.quota),
                        $('<li/>').html('Expires: ' + data.expires)
                      ]))
                      .append($('<p/>').html('Current size: '+data.currentSize));
              for(var p=0; p<data.projects.length; p++)
                $('#result').append($('<hr/>')).append($('<p/>').html('Title: '+data.projects[p].title));
            }
          })
          .fail(function (jqXHR, textStatus, errorThrown) {
            $('#result').html('ERROR: ' + textStatus + ' - ' + errorThrown);
            console.log(errorThrown);
            console.log(jqXHR);
          }).always(function () {
    // TODO: Remove waiting image
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


