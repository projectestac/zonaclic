const path = require('path');
const fs = require('fs-extra');
const ch = require('chalk');
const { createFilePath } = require('gatsby-source-filesystem');
const { siteMetadata: { defaultLanguage, supportedLanguages } } = require('./gatsby-config')

const activeEnv = process.env.GATSBY_ACTIVE_ENV || process.env.NODE_ENV || 'development';
require('dotenv').config({
  path: `.env.${activeEnv}`,
});
const PATH_PREFIX = process.env.PATH_PREFIX || '';
// const PATH_PREFIX = `${__PATH_PREFIX__}/`;

const STOP_WORDS = {};
supportedLanguages.forEach(lang => {
  const fName = path.join(__dirname, `src/intl/stopwords-${lang}.json`);
  STOP_WORDS[lang] = fs.existsSync(fName) ? require(fName) : [];
});

function getTextTokens(text, lang) {
  text = text
    // Remove URLS
    .replace(/https?:[-/.\w?=#&%@]+/g, '')
    // Remove ISO dates
    .replace(/\d{4}-\d{2}-\d{2}T[-\w.:]+/g, '')
    // Take symbols as separators
    .replace(/[-_\s(){}[\]#*<>,.;:¿?/'@~=+\\|¡!"£$€^&`´]+/g, ' ')
    // Convert all to lower case
    .toLowerCase();

  // Convert text to an array of unique words
  const tokens = Array.from(new Set(text.split(' ')))
    // Exclude stopwords and single chars
    .filter(token => token.length > 1 && !STOP_WORDS[lang].includes(token))
    // Sort list
    .sort();

  return tokens.join(' ').trim();
}

exports.onCreateNode = ({ node, getNode, actions: { createNodeField } }) => {

  if (node.internal.type === 'Mdx') {
    const path = createFilePath({ node, getNode });
    let slug = path;
    let lang = defaultLanguage;
    const matches = path.match(/\/([a-z-_]*)\/$/);
    if (matches && matches.length === 2) {
      lang = matches[1];
      slug = slug.substr(0, slug.length - lang.length - 1);
    }
    if (/\/content\/blog\//.test(node.fileAbsolutePath))
      slug = `/blog${slug}`;

    // Create node fields
    createNodeField({
      name: 'slug',
      node,
      value: slug,
    });
    createNodeField({
      name: 'lang',
      node,
      value: lang,
    });
    createNodeField({
      name: 'tokens',
      node,
      value: getTextTokens(node.rawBody, lang),
    });
  }
};

exports.createPages = async ({ graphql, actions: { createPage } }) => {

  const result = await graphql(`
    query {
      allMdx(filter: {fields: {lang: {eq: "${defaultLanguage}"}}}, sort: {fields: frontmatter___date, order: DESC}) {
        edges {
          node {
            parent {
              ... on File {
                sourceInstanceName
              }
            }
            fields {
              lang
              slug
            }
            frontmatter {
              title
            }
          }
        }
      }
    }
  `);

  if (result.errors) {
    throw result.errors;
  }

  // Create info pages.
  const pageTemplate = path.resolve('./src/templates/StaticPage.js');
  const pages = result.data.allMdx.edges.filter(post => post.node.parent.sourceInstanceName === 'static');
  pages.forEach(({ node: { fields: { slug } } }) => {
    createPage({
      path: slug,
      component: pageTemplate,
      context: {
        slug,
      },
    });
  });

  // Create blog posts.
  const blogPostTemplate = path.resolve('./src/templates/BlogPost.js');
  const posts = result.data.allMdx.edges.filter(post => post.node.parent.sourceInstanceName === 'blog');
  posts.forEach(({ node: { fields: { slug } } }, index) => {
    const previous = index === posts.length - 1 ? null : posts[index + 1].node;
    const next = index === 0 ? null : posts[index - 1].node;
    createPage({
      path: slug,
      component: blogPostTemplate,
      context: {
        slug,
        previous,
        next,
      },
    });
  });
};

// See: https://github.com/gatsbyjs/gatsby/issues/564#issuecomment-527891177
exports.onCreateWebpackConfig = ({ actions: { setWebpackConfig } }) => {
  setWebpackConfig({
    node: {
      fs: 'empty',
      net: 'empty',
    }
  })
};

// Move build files into the prefixed path, if defined
function moveBuildToPathPrefix() {
  if (PATH_PREFIX && PATH_PREFIX.startsWith('/')) {
    const buildDirName = 'public';
    const buildDir = path.resolve(__dirname, buildDirName);
    const prefix = `.${PATH_PREFIX}`;
    const prefixedDir = path.resolve(__dirname, prefix);
    if (fs.existsSync(prefixedDir)) {
      console.log(`${ch.bold.red('error:')} directory "${prefixedDir}" alredy exists. Unable to move build files to "${PATH_PREFIX}"`);
      return;
    }
    const destDir = path.resolve(__dirname, buildDirName, prefix);
    fs.renameSync(buildDir, prefixedDir);
    fs.moveSync(prefixedDir, destDir);
    // Create a symlink for 404
    fs.symlinkSync(`${prefix}/404.html`, `${buildDir}/404.html`);
    console.log(`${ch.bold.green('info:')} Build files moved to "${destDir}"`);
  }
}

exports.onPostBuild = async function onPostBuild() {
  moveBuildToPathPrefix();
};
