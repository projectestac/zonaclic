import React, { useState, useEffect } from 'react';
import { graphql } from 'gatsby';
import Fuse from 'fuse.js';
import { makeStyles } from '@material-ui/core/styles';
import Layout from '../components/Layout';
import SEO from '../components/SEO';
import { useIntl, navigate } from 'gatsby-plugin-intl';
import queryString from 'query-string';
import CircularProgress from '@material-ui/core/CircularProgress';
import Typography from '@material-ui/core/Typography';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemAvatar from '@material-ui/core/ListItemAvatar';
import Avatar from '@material-ui/core/Avatar';
import ListItemText from '@material-ui/core/ListItemText';
import TablePagination from '@material-ui/core/TablePagination';
import { FontAwIcon } from '../utils/FontAwIcon';
import { checkFetchResponse } from '../utils/misc';
import { repoSlug } from './repo';

const SLUG = '/search/';
const DEFAULT_ITEMS_PER_PAGE = 25;

// Fuse.js options
// See: https://fusejs.io/
const FUSE_OPTIONS = {
  caseSensitive: false,
  shouldSort: true,
  tokenize: true,
  matchAllTokens: true,
  includeScore: false,
  includeMatches: false,
  threshold: 0.2,
  location: 0,
  distance: 4,
  maxPatternLength: 32,
  minMatchCharLength: 2,
};

const useStyles = makeStyles(theme => ({
  root: {
    '& h1': { ...theme.typography.h1, color: theme.palette.primary.dark, },
    '& h2': { ...theme.typography.h2, color: theme.palette.primary.dark, },
  },
  spacer: {
    display: 'none',
  },
  toolbar: {
    flexFlow: 'wrap',
  },
  waiting: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    "& > *": {
      margin: '1rem',
    },
  },
}));


export default function Search({ location, data }) {

  const { allMdx: { nodes }, site: { siteMetadata: { repoBase, repoList, jclicSearchService } } } = data;
  const classes = useStyles();
  const intl = useIntl();
  const { locale: lang, messages, formatMessage } = intl;

  const [results, setResults] = useState([]);
  const [waiting, setWaiting] = useState(true);
  const [title, setTitle] = useState('');
  const [query, setQuery] = useState(queryString.parse(location.search)['query'] || '');
  const [page, setPage] = React.useState(0);
  const [itemsPerPage, setItemsPerPage] = React.useState(DEFAULT_ITEMS_PER_PAGE);

  const fuseEngine = {};

  function getFuseEngine(locale) {
    if (!fuseEngine[locale])
      fuseEngine[locale] = new Fuse(
        nodes
          .filter(({ fields: { lang } }) => lang === locale)
          .map(({ fields: { slug, tokens }, frontmatter: { title, description, icon } }) => ({
            title,
            description,
            slug,
            icon,
            tokens,
          })),
        { ...FUSE_OPTIONS, keys: ['tokens'] }
      );
    return fuseEngine[locale];
  }

  function performQuery() {
    setTitle(formatMessage({ id: 'search-results' }, { query }));
    setWaiting(true);
    // Delay the search operation, so allowing page to be fully rendered
    window.setTimeout(() => {
      const fuse = getFuseEngine(lang);
      const staticResults = fuse.search(query);
      setResults(staticResults);
      fetch(`${jclicSearchService}?lang=${lang}&method=boolean&q=${encodeURIComponent(query)}`)
        .then(checkFetchResponse)
        .then(textMatches => {
          if (textMatches.length) {
            return fetch(repoList)
              .then(checkFetchResponse)
              .then(fullProjectList => {
                setResults([]
                  .concat(staticResults)
                  .concat(fullProjectList.filter(({ path }) => textMatches.includes(path)))
                );
                setWaiting(false);
              });
          }
          setWaiting(false);
        })
        .catch(err => console.log(err?.toString() || 'Error'));
    }, 0);
  }

  useEffect(performQuery, [query]);
  useEffect(() => setQuery(queryString.parse(location.search)['query'] || ''), [location.search]);

  return (
    <Layout {...{ intl, slug: SLUG }}>
      <SEO {...{ lang, title, location, slug: SLUG }} />
      <article className={classes.root}>
        <header>
          <h2>{title}</h2>
        </header>
        <hr />
        {
          (!waiting && results.length === 0 && <h2>{messages['no-results']}</h2>) ||
          <div>
            <List>
              {results
                .slice(page * itemsPerPage, (page + 1) * itemsPerPage)
                .map((result, n) => {
                  if (result.path) {
                    const { path, title, author, cover, thumbnail } = result;
                    return (
                      <ListItem button key={n} onClick={() => navigate(`${repoSlug}?act=${path}`)}>
                        <ListItemAvatar>
                          <Avatar variant="square" alt={title} src={`${repoBase}/${path}/${thumbnail || cover}`} />
                        </ListItemAvatar>
                        <ListItemText primary={title} secondary={author} />
                      </ListItem>
                    );
                  } else {
                    const { slug, title, description, icon } = result;
                    return (
                      <ListItem button key={n} onClick={() => navigate(slug)}>
                        <ListItemAvatar>
                          <Avatar>
                            <FontAwIcon icon={icon} size="lg" />
                          </Avatar>
                        </ListItemAvatar>
                        <ListItemText primary={title} secondary={description} />
                      </ListItem>
                    );
                  }
                })
              }
            </List>
            <hr />
            <TablePagination
              classes={{ spacer: classes.spacer, toolbar: classes.toolbar }}
              component="nav"
              page={page}
              rowsPerPage={itemsPerPage}
              onChangeRowsPerPage={ev => setItemsPerPage(ev.target.value)}
              count={results.length}
              onChangePage={(_ev, p) => setPage(p)}
              labelDisplayedRows={({ from, to, count }) => formatMessage({ id: 'results-count' }, { from, to, count })}
              labelRowsPerPage={messages['results-per-page']}
              backIconButtonText={messages['results-page-prev']}
              nextIconButtonText={messages['results-page-next']}
            />
          </div>
        }
        {waiting &&
          <div className={classes['waiting']}>
            <Typography variant="subtitle1">{messages['searching']}</Typography>
            <CircularProgress size="4rem" />
          </div>
        }
      </article>
    </Layout>
  );
}

export const pageQuery = graphql`
  query {
    site {
      siteMetadata {
        repoBase
        repoList
        jclicSearchService
      }
    }
    allMdx {
      nodes {
        fields {
          lang
          slug
          tokens          
        }
        frontmatter {
          title
          description
          icon
        }
      }
    }
  }
`;
