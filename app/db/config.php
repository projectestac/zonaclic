<?php

// ** Proxy settings ** //
define('USE_PROXY', true); // 'true' in FMO
define('PROXY_HOST', 'squid');
define('PROXY_PORT', 3128);

// ** MySQL settings ** //
define('DB_HOST', 'mysql');
define('DB_NAME', 'jclicrepo');
define('DB_USER', 'clic');
define('DB_PASSWORD', 'clic');
define('DB_CHARSET', 'utf8');
define('DB_COLLATE', '');

// ** Path to repository projects ** //
define('PROJECTS_PATH', '../../web/projects');
define('PROJECTS_INDEX', 'projects.json');

// ** Languages currently used in this repository ** //
define('LANGS', ['ca', 'es', 'en']);

// ** Possible MySQL full text search methods ** //
define('FTS_METHODS', ['natural', 'boolean']);

// ** Default disk quota for unlisted users is 50 MB ** //
define('DEFAULT_QUOTA', 52428800);

// ** Path to 'users' root ** //
define('USERS_ROOT', '../../web/users');
define('SETTINGS_FILE', 'settings.json');

// ** Parameter name for Google OAuth token ** //
define('ID_TOKEN', 'id_token');

// ** Endpoint used to validate OAuth tokens sent by Google ** //
define('CHECK_GOOGLE_TOKEN', 'https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=');
define('CHECK_GOOGLE_TOKEN_NEW_API', 'https://oauth2.googleapis.com/tokeninfo?id_token=');
define('USE_NEW_API', 'NEW_API');

// ** Users belonging to this Google Suite organisation are accepted ** //
define('HD', 'xtec.cat');

// Current process UID
define('UID', posix_geteuid());

// Valid origins for userlib clients
define('VALID_ORIGINS', [
  'https://clic.xtec.cat',
  'https://preproduccio.clic.xtec.cat',
  'https://projectes.xtec.cat',
  'https://agora.xtec.cat',
  'https://educaciodigital.cat',
  'https://met.xtec.cat',
  'https://clic.xyz',
  'http://localhost:8000'
]);
