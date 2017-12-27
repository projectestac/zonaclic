<?php

// ** MySQL settings ** //
define('DB_HOST', 'localhost');
define('DB_NAME', 'jclicrepo');
define('DB_USER', 'clic');
define('DB_PASSWORD', 'clic');
define('DB_CHARSET', 'utf8');
define('DB_COLLATE', '');

// ** Path to repository projects ** //
define('PROJECTS_PATH', '../../projects');
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

// ** Users belonging to this Google Suite organisation are accepted ** //
define('HD', 'xtec.cat');
