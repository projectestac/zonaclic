/* global $, window, dialogPolyfill, clipboard */

var root = 'https://clic.xtec.cat/users/';
//var root = window.location.origin + window.location.pathname;
var usrLibRoot = root;

$(function () {

  initShareDlg();

  var user = window.location.search.substring(1);
  if (user === null || user === '' || /[^A-Za-z0-9_.]/gi.test(user)) {
    logError('Usuari desconegut!');
  } else {
    usrLibRoot = root + user + '/';
    $.ajax({
      dataType: 'json',
      url: usrLibRoot + 'projects.json',
      success: function (projects) {
        $('#userIdBox').html(user);
        var $projects = $('#projects');
        for (var p = 0; p < projects.length; p++) {
          var prj = projects[p];
          if (prj.path) {
            var prjBasePath = prj.path;
            var prjInfoPath = usrLibRoot + prj.path + '/project.json';
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
                  logError('ERROR loading "' + prjInfoPath + '": ' + xhr.statusText);
                }
              });
            })(prjBasePath, prjInfoPath);
          }
        }
      },
      error: function (xhr) {
        logError('ERROR: ' + xhr.statusText);
      }
    });
  }
});

function logError(msg) {
  console.log(error);
  $('#msg').append($('<p/>').html('ERROR loading "' + prjInfoPath + '": ' + xhr.statusText)).removeClass('hidden');
}


// Initialize the 'share project' dialog
function initShareDlg() {
  var $shareDlg = $('#shareDlg');
  dialogPolyfill.registerDialog($shareDlg[0]);
  $('.shareText')
    .on('focus', function () { this.setSelectionRange(0, this.value.length); })
    .attr('spellcheck', false);
  $('#linkTextCopy').on('click', function () {
    clipboard.copy($('#directLink').val())
      .then(function () {
        $('#copy-toast')[0].MaterialSnackbar.showSnackbar({ message: 'L\'enllaç s\'ha copiat al porta-retalls' });
      });
  });
  $('#embedCodeCopy').on('click', function () {
    clipboard.copy($('#embedCode').val())
      .then(function () {
        $('#copy-toast')[0].MaterialSnackbar.showSnackbar({ message: 'El codi s\'ha copiat al porta-retalls. Enganxeu-lo amb CTRL+V a la pàgina o article del blog on vulgueu que aparegui.' });
      });
  });
  $('#moodleLinkCopy').on('click', function () {
    clipboard.copy($('#moodleLink').val())
      .then(function () {
        $('#copy-toast')[0].MaterialSnackbar.showSnackbar({ message: 'L\'enllaç s\'ha copiat al porta-retalls. Enganxeu-lo amb CTRL+V en una activitat de tipus JClic de Moodle.' });
      });
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

  var $playBtn = $('<a/>', {
    class: 'mdl-button mdl-button--icon mdl-button--raised mdl-button--accent mdl-js-button mdl-js-ripple-effect',
    title: 'Obre el projecte',
    href: basePath + 'index.html',
    target: '_blank'
  })
    .append($('<i/>', { class: 'material-icons' }).html('play_arrow'));

  // Build card
  $result.append($('<div/>', { class: 'mdl-card__actions mdl-card--border' })
    .append($downloadBtn, $shareBtn));

  $result.append($('<div/>', { class: 'mdl-card__menu' })
    .append($playBtn));

  return $result;
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
