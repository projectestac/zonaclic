
var mt = "mailto:";

function MM_swapImgRestore() { //v3.0
  var i, x, a = document.MM_sr;
  for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++)
    x.src = x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d = document;
  if (d.images) {
    if (!d.MM_p) d.MM_p = new Array();
    var i, j = d.MM_p.length, a = MM_preloadImages.arguments;
    for (i = 0; i < a.length; i++) {
      if (a[i].indexOf("#") != 0) {
        d.MM_p[j] = new Image;
        d.MM_p[j++].src = a[i];
      }
    }
  }
}

function MM_findObj(n, d) { //v4.01
  var p, i, x;
  if (!d) d = document;
  if ((p = n.indexOf("?")) > 0 && parent.frames.length) {
    d = parent.frames[n.substring(p + 1)].document;
    n = n.substring(0, p);
  }
  if (!(x = d[n]) && d.all)
    x = d.all[n];
  for (i = 0; !x && i < d.forms.length; i++)
    x = d.forms[i][n];
  for (i = 0; !x && d.layers && i < d.layers.length; i++)
    x = MM_findObj(n, d.layers[i].document);
  if (!x && d.getElementById)
    x = d.getElementById(n);
  return x;
}

function MM_swapImage() { //v3.0
  var i, j = 0, x, a = MM_swapImage.arguments;
  document.MM_sr = new Array;
  for (i = 0; i < (a.length - 2); i += 3) {
    if ((x = MM_findObj(a[i])) != null) {
      document.MM_sr[j++] = x;
      if (!x.oSrc) x.oSrc = x.src;
      x.src = a[i + 2];
    }
  }
}

function mailto(a) {
  window.location.href = mt + a.split("-dot-").join(".").replace("-at-", "@").split("").reverse().join("");
}

// Add redirection to new website

const currentLang = document.location.href.indexOf('/ca/') > 0 ? 'ca' : document.location.href.indexOf('/es/') > 0 ? 'es' : 'en';
const redirectURL = `https://projectes.xtec.cat/clic/${currentLang}/`;
const secondsToJump = 10;

const templateBodies = {
  ca: `
<h1><strong>ATENCI&Oacute;</strong>: Esteu veient una versi&oacute; antiga,<br/>ja arxivada, de la zonaClic</h1>
<p id="redirectCount">El navegador us redirigir&agrave; a la <strong><a href="${redirectURL}" id="jumper">nova web</a></strong> en <span id="timeLeft">${secondsToJump}</span> segons.</p>
<p id="redirectStop">Feu clic <a href="#" id="stopBtn">aqu&iacute;</a> si voleu evitar la redirecci&oacute; i romandre en aquesta p&agrave;gina.</p>
<p id="linkMsg" class="hidden"><strong><a href="${redirectURL}">Feu clic aqu&iacute; per anar a la nova web</a></strong></p>
<p id="infoMsg" class="hidden">S'est&agrave; redirigint a la nova web...</p>`,
  es: `
<h1><strong>ATENCI&Oacute;N</strong>: Est&aacute; viendo una versi&oacute;n antigua,<br/>ya archivada, de la zonaClic</h1>
<p id="redirectCount">El navegador le redirigir&aacute; a la <strong><a href="${redirectURL}" id="jumper">nueva web</a></strong> en <span id="timeLeft">${secondsToJump}</span> segundos.</p>
<p id="redirectStop">Haga clic <a href="#" id="stopBtn">aqu&iacute;</a> para evitar la redirecci&oacute;n y permanecer en esta p&aacute;gina.</p>
<p id="linkMsg" class="hidden"><strong><a href="${redirectURL}">Haga clic aqu&iacute; para ir a la nueva web</a></strong></p>
<p id="infoMsg" class="hidden">Redirigiendo a la nueva web...</p>`,
  en: `
<h1><strong>WARNING</strong>: You are viewing an old, archived<br/>version of the ClicZone</h1>
<p id="redirectCount">The web browser will redirect you to the <strong><a href="${redirectURL}" id="jumper">new site</a></strong> within <span id="timeLeft">${secondsToJump}</span> seconds.</p>
<p id="redirectStop">Click <a href="#" id="stopBtn">here</a> if you want to avoid the redirect and stay on this page.</p>
<p id="linkMsg" class="hidden"><strong><a href="${redirectURL}">Click here to go to the new site</a></strong></p>
<p id="infoMsg" class="hidden">Redirecting to the new site...</p>`,
};

const redirectTemplate = document.createElement('template');
redirectTemplate.innerHTML = `
<style>

  div {
    max-width: 750px;
    border: 4px solid red;
    background-color: beige;
    margin: 60px auto;
    text-align: center;
  }

  h1 {
    font-size: 18pt;
    color: #003300;
    font-family: "Trebuchet MS", "Lucida Grande", Verdana, Lucida, Geneva, Helvetica, Arial, sans-serif;
  }

  .hidden {
    display: none;
  }

</style>

<div>
${templateBodies[currentLang]}
</div>
`;

class RedirectElement extends HTMLElement {

  connectedCallback() {

    this.attachShadow({ mode: 'open' });
    this.shadowRoot.appendChild(redirectTemplate.content.cloneNode(true));

    const skipRedirect = sessionStorage.getItem('skipRedirect') === 'true' && sessionStorage.getItem('lastUrl') !== window.location.href;
    if (!skipRedirect)
      sessionStorage.setItem('skipRedirect', 'false');
    sessionStorage.setItem('lastUrl', window.location.href);

    const timeTag = this.shadowRoot.getElementById('timeLeft');
    const redirectCount = this.shadowRoot.getElementById('redirectCount');
    const redirectStop = this.shadowRoot.getElementById('redirectStop');
    const infoMsg = this.shadowRoot.getElementById('infoMsg');
    const linkMsg = this.shadowRoot.getElementById('linkMsg');
    const jumper = this.shadowRoot.getElementById('jumper');
    const stopBtn = this.shadowRoot.getElementById('stopBtn');

    let timeLeft = secondsToJump;

    const timer = setInterval(() => {
      timeTag.innerHTML = --timeLeft;
      if (timeLeft < 1) {
        clearInterval(timer);
        redirectCount.classList.add('hidden');
        redirectStop.classList.add('hidden');
        infoMsg.classList.remove('hidden');
        jumper.click();
      }
    }, 1000);

    function stopTimer() {
      clearInterval(timer);
      redirectCount.classList.add('hidden');
      redirectStop.classList.add('hidden');
      infoMsg.classList.add('hidden');
      linkMsg.classList.remove('hidden');
    }

    if (skipRedirect)
      stopTimer();

    stopBtn.addEventListener('click', (ev) => {
      ev.preventDefault();
      stopTimer();
      sessionStorage.setItem('skipRedirect', 'true');
    });
  }
}
customElements.define("redirect-element", RedirectElement);

document.addEventListener('DOMContentLoaded', () => {
  document.body.prepend(document.createElement('redirect-element'));
}, false);
