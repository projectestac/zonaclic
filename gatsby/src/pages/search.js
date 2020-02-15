import React, { useState, useEffect } from 'react';
import { graphql } from 'gatsby';
import Fuse from 'fuse.js';
import { makeStyles } from '@material-ui/core/styles';
import Layout from '../components/Layout';
import SEO from '../components/SEO';
import { useIntl, navigate } from 'gatsby-plugin-intl';
import queryString from 'query-string';
import Typography from '@material-ui/core/Typography';
import CircularProgress from '@material-ui/core/CircularProgress';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemAvatar from '@material-ui/core/ListItemAvatar';
import Avatar from '@material-ui/core/Avatar';
import ListItemText from '@material-ui/core/ListItemText';
import TablePagination from '@material-ui/core/TablePagination';
import { FontAwIcon } from '../utils/FontAwIcon';

const SLUG = '/search/';
const DEFAULT_ITEMS_PER_PAGE = 10;

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
  spacer: {
    display: 'none',
  },
  toolbar: {
    flexFlow: 'wrap',
  },
}));


export default function Search({ location, data }) {

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
        data.allMdx.nodes
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
      setResults(fuse.search(query));
      setWaiting(false);
    }, 0);
  }

  useEffect(performQuery, [query]);
  useEffect(() => setQuery(queryString.parse(location.search)['query'] || ''), [location.search]);

  return (
    <Layout {...{ intl, slug: SLUG }}>
      <SEO {...{ lang, title, location, slug: SLUG }} />
      <article>
        <header>
          <Typography variant="h2" gutterBottom>{title}</Typography>
        </header>
        <hr />
        {
          (waiting && <CircularProgress />) ||
          (results.length === 0 && <h2>{messages['no-results']}</h2>) ||
          <div>
            <List>
              {results.slice(page * itemsPerPage, (page + 1) * itemsPerPage).map(({ slug, title, description, icon }, n) => (
                <ListItem button key={n} onClick={() => navigate(slug)}>
                  <ListItemAvatar>
                    <Avatar>
                      <FontAwIcon icon={icon} size="lg" />
                    </Avatar>
                  </ListItemAvatar>
                  <ListItemText primary={title} secondary={description} />
                </ListItem>
              ))}
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
      </article>
    </Layout>
  );
}

export const pageQuery = graphql`
  query MyQuery {
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
