/* global $, gapi */

var url = new URL(window.location.href);
var usrLibRoot = url.protocol + '//' + url.host + '/users/';

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

  $.post('/db/getUserInfo', {id_token: googleUser.getAuthResponse().id_token}, null, 'json')
          .done(function (data) {
            if (data === null || typeof data !== 'object')
              $('#userInfo').html('ERROR: No data received!');
            else if (data.status !== 'validated')
              $('#userInfo').html('ERROR: ' + data.error);
            else {
              $('#userInfo')
                      .append($('<img/>', {src: data.avatar}))
                      .append($('<ul/>').append([
                        $('<li/>').html('User: ' + data.fullUserName),
                        $('<li/>').html('Email: ' + data.email),
                        $('<li/>').html('Id: ' + data.id),
                        $('<li/>').html('Quota: ' + data.quota),
                        $('<li/>').html('Expires: ' + data.expires)
                      ]))
                      .append($('<p/>').html('Current size: ' + data.currentSize));

              for (var p = 0; p < data.projects.length; p++)
                $('#userProjects').append(getProjectElement$(data.projects[p]));

              $('#uploadForm').append($('<form/>', {id: 'upFile', enctype: 'multipart/form-data'})
                      .append($('<input/>', {type: 'file', name: 'scormFile', accept: '.scorm.zip'}).on('change', function () {
                        var file = this.files[0];
                        console.log('File is: ' + file.name + ' (' + file.size + ' - ' + file.type + ')');
                      }))
                      .append($('<input/>', {type: 'text', name: 'project'}))
                      .append($('<input/>', {type: 'button', value: 'Upload'}).on('click', function () {
                        var formData = new FormData($('#upFile')[0]);
                        $.ajax({
                          url: '/db/uploadUserFile',
                          type: 'POST',
                          xhr: function () {  // Custom XMLHttpRequest
                            var myXhr = $.ajaxSettings.xhr();
                            if (myXhr.upload) { // Check if upload property exists
                              myXhr.upload.addEventListener('progress', progressHandlingFunction, false); // For handling the progress of the upload
                            }
                            return myXhr;
                          },
                          //Ajax events
                          beforeSend: beforeSendHandler,
                          success: completeHandler,
                          error: errorHandler,
                          // Form data
                          data: formData,
                          //Options to tell jQuery not to process data or worry about content-type.
                          cache: false,
                          contentType: false,
                          processData: false
                          //dataType: 'json'
                        });
                      }))).append($('<progress/>', {id: 'upProgress'}));
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

function progressHandlingFunction(e) {
  if (e.lengthComputable) {
    $('#upProgress').attr({value: e.loaded, max: e.total});
  }
}

function beforeSendHandler(e) {
  console.log('Before send handler called');
  console.log(e);
}

function completeHandler(d) {
  console.log('Complete handler called');
  console.log(d);
  if (d.project)
    $('#userProjects').append(getProjectElement$(d.project));
}

function errorHandler(e) {
  console.log('Error handler called:');
  console.log(e);
}

function getProjectElement$(project) {
  var basePath = usrLibRoot + project.basePath + '/';
  console.log(project);
  var result$ = $('<div/>', {class: 'project mdl-card mdl-shadow--2dp'});
  result$.append($('<ul/>')
          .append($('<li/>').html('Name: ' + project.name))
          .append($('<li/>').html('Title: ' + project.title))
          .append($('<li/>').html('Author: ' + project.author))
          .append($('<li/>').html('School: ' + project.school))
          .append($('<li/>').append($('<img/>', {src: basePath + project.thumbnail})))
          .append($('<li/>').append($('<a/>', {href: basePath + 'index.html', target: '_blank'}).html('Link to project')))
          .append($('<li/>').html('DELETE').on('click', function () {
            $.ajax({
              url: '/db/deleteProject',
              type: 'POST',
              data: {project: project.name},
              success: function (e) {
                if (e.status === 'ok')
                  result$.remove();
              },
              error: function (e) {
                console.log('Error:' + e);
              },
              cache: false,
              dataType: 'json'
            });
          }))
          );
  return result$;
}
