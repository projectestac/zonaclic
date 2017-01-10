/* global $, window, dialogPolyfill */
$(function () {
  var user = window.location.search.substring(1);
  //var basePath = 'https://clic.xtec.cat/users';
  var basePath = '.';

  if (user === null || user === '' || /[^A-Za-z0-9_.]/gi.test(user)) {
    $('#msg').html('Usuari desconegut').removeClass('hidden');
  } else {
    basePath = user;
    $.ajax({
      dataType: 'json',
      url: basePath + '/projects.json',
      success: function (projects) {
        $('#userIdBox').html(user);
        var $projects = $('#projects');
        for (var p = 0; p < projects.length; p++) {
          var prj = projects[p];
          if (prj.path) {
            var prjBasePath = basePath + '/' + prj.path;
            var prjInfoPath = prjBasePath + '/project.json';
            // Enclose ajax call in a closure to retain path values
            (function (prjBasePath, prjInfoPath) {
              $.ajax({
                dataType: 'json',
                url: prjInfoPath,
                success: function (project) {
                  project.basePath = prjBasePath;
                  $projects.append($buildProjectCard(project));
                },
                error: function (xhr) {
                  console.log('Error loading ' + prjInfoPath + ': ' + xhr.statusText);
                }
              });
            })(prjBasePath, prjInfoPath);
          }
        }
      },
      error: function (xhr) {
        $('#msg').html('ERROR: ' + xhr.statusText).removeClass('hidden');
      }
    });
  }

  function $buildProjectCard(project) {
    var basePath = project.basePath + '/';
    var $result = $('<div/>', {class: 'project mdl-cell mdl-card mdl-shadow--2dp'}).data('project', project);

    $result.append($('<div/>', {class: 'mdl-card__title'}).css({background: 'url(\'' + basePath + project.cover + '\') center / cover'})
            .append($('<h2/>', {class: 'mdl-card__title-text'}).html(project.title)));

    var lang = project.meta_langs && project.meta_langs.length > 0 ? project.meta_langs[0] : 'ca';

    var $cardBody = $('<div/>', {class: 'mdl-card__supporting-text'}).append(
            $('<p/>', {class: 'prjAuthor'}).html(project.author),
            $('<p/>', {class: 'prjSchool'}).html(project.school));

    var $tBody = $('<tbody/>').append(
            $('<tr/>').append($('<td>').html('Data:'), $('<td>').html(project.date)));

    if (project.languages && project.languages[lang])
      $tBody.append($('<tr/>').append($('<td>').html('Idiomes:'), $('<td>').html(project.languages[lang])));

    if (project.levels && project.levels[lang])
      $tBody.append($('<tr/>').append($('<td>').html('Nivells:'), $('<td>').html(project.levels[lang])));

    if (project.areas && project.areas[lang])
      $tBody.append($('<tr/>').append($('<td>').html('Àrees:'), $('<td>').html(project.areas[lang])));

    $cardBody.append($('<table/>', {class: 'prjData'}).append($tBody));

    if (project.description && project.description[lang])
      $cardBody.append($('<p/>', {class: 'prjDesc'}).html(project.description[lang]));

    $result.append($cardBody);
    project.card = $result;

    // Create action buttons:
    var $shareBtn = $('<button/>', {
      class: 'mdl-button mdl-button--icon mdl-button--colored mdl-js-button mdl-js-ripple-effect',
      title: 'Comparteix...'})
            .append($('<i/>', {class: 'material-icons'}).html('share').on('click', function () {
              openShareDlg(project);
            }));

    var $downloadBtn = $('<a/>', {
      class: 'mdl-button mdl-button--icon mdl-button--colored mdl-js-button mdl-js-ripple-effect',
      title: 'Descarrega el fitxer',
      download: true,
      href: '/db/downloadUserProject?prj=' + project.basePath})
            .append($('<i/>', {class: 'material-icons'}).html('cloud_download'));

    var $playBtn = $('<a/>', {
      class: 'mdl-button mdl-button--icon mdl-button--raised mdl-button--accent mdl-js-button mdl-js-ripple-effect',
      title: 'Obre el projecte',
      href: basePath + 'index.html',
      target: '_BLANK'})
            .append($('<i/>', {class: 'material-icons'}).html('play_arrow'));

    // Build card
    $result.append($('<div/>', {class: 'mdl-card__actions mdl-card--border'})
            .append($downloadBtn, $shareBtn));

    $result.append($('<div/>', {class: 'mdl-card__menu'})
            .append($playBtn));

    return $result;
  }

  var shareDlg = $('#shareDlg')[0];
  dialogPolyfill.registerDialog(shareDlg);
  $('#closeDlg').on('click', function(){shareDlg.close();});

  function openShareDlg(project) {
    shareDlg.showModal();
  }

});
