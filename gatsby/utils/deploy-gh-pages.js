#!/usr/bin/env node

const path = require('path');
const fs = require('fs');
const ghpages = require('gh-pages');
const fetch = require('node-fetch');
const ch = require('chalk');
let { version, homepage } = require('../package.json');

const FORCE = process.argv.length > 2 && process.argv[2] === '--force';

const activeEnv = process.env.NODE_ENV || 'production';
require('dotenv').config({
  path: path.join(__dirname, `../.env.${activeEnv}`),
});

const BUILD_DIR = path.join(__dirname, '../public');

// Setting "history" to false avoids unnecessary increment of the global repository size
const GH_PAGES_HISTORY = (process.env.GH_PAGES_HISTORY || 'false') !== 'true';
const GH_PAGES_REMOTE = process.env.GH_PAGES_REMOTE || 'origin';
const GH_PAGES_BRANCH = process.env.GH_PAGES_BRANCH || 'gh-pages';

const BASE_URL = process.env.BASE_URL || homepage || '';
const PATH_PREFIX = process.env.PATH_PREFIX || '';

// Check version
if (!version) {
  console.log(`${ch.bold.yellowBright('WARNING:')} No 'version' tag specified in 'package.json'.`);
  version = 'unknown';
}

// Check if build directory exists
if (!fs.existsSync(BUILD_DIR)) {
  console.log(`${ch.bold.red('ERROR:')} 'public' directory not found. Please launch 'npm run build' before deploying a new version.`);
  process.exit(1);
}

function checkDeployedVersion() {
  if (!BASE_URL)
    return Promise.resolve({ err: 'No "URL_BASE" provided. Unable to check current deployed version' });

  return fetch(`${BASE_URL}manifest.webmanifest`)
    .then(response => {
      if (!response.ok)
        throw new Error('Unable to check the current deployment.');
      return response;
    })
    .then(response => response.json())
    .then(({ description }) => {
      if (!description || !description.match(/\(([\w.-]+)\)$/))
        throw new Error(`Unable to retrieve the version tag from the currently deployed file 'manifest.webmanifest'.`);
      return { deployedVersion: description.match(/\(([\w.-]+)\)$/)[1] };
    })
    .catch(err => {
      return Promise.resolve({ err });
    });
}

async function main() {
  const { err, deployedVersion } = await checkDeployedVersion();
  if (!FORCE && (err || deployedVersion === version)) {
    if (err)
      console.log(`${ch.bold.red('WARNING:')} ${err}`);
    else
      console.log(`${ch.bold.red('WARNING:')} Current version (${version}) is already deployed.`);

    console.log(`Launch ${ch.bold.whiteBright('deploy-gh-pages --force')} to ignore this warning and (re)deploy the current build ${version}.\n`);
    return;
  }

  console.log(`${ch.bold.green('INFO:')} Deploying build "${version}" over "${deployedVersion}" on Github Pages.`);

  ghpages.publish(
    BUILD_DIR,
    {
      branch: GH_PAGES_BRANCH,
      dest: `.${PATH_PREFIX}`,
      dotfiles: true,
      add: false,
      remote: GH_PAGES_REMOTE,
      message: `Version ${version || 'unknown'}`,
      history: GH_PAGES_HISTORY,
    },
    (err) => {
      if (err)
        console.log(`${ch.bold.red('ERROR:')} ${err}`);
      else
        console.log(`${ch.bold.green('INFO:')} Version ${version} successfully deployed to: ${BASE_URL}`);
    }
  );
}

// Main process starts here
try {
  main();
} catch (err) {
  console.log(ch.bold.red(`ERROR: ${err}`));
}

