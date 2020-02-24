import React, { useState, useMemo } from 'react';
import queryString from 'query-string';
import { useIntl } from 'gatsby-plugin-intl';
import { makeStyles } from '@material-ui/core/styles';
import Layout from '../components/Layout';
import RepoMain from '../components/repo/RepoMain';

const SLUG = '/repo/';

const useStyles = makeStyles(theme => ({
  root: {
    '& h1': { ...theme.typography.h1, color: theme.palette.primary.dark, },
    '& h2': { ...theme.typography.h2, color: theme.palette.primary.dark, },
  },
}));

export default function Repo({ location }) {

  const classes = useStyles();
  const intl = useIntl();
  const [act, setAct] = useState();
  useMemo(() => setAct(queryString.parse(location.search)['act']), [location]);

  // SEO defined in inner elements

  return (
    <Layout {...{ intl, slug: `${SLUG}${act ? `${act}/` : ''}` }}>
      <article className={classes.root}>
        <RepoMain {...{ intl, location, SLUG, act }} />
      </article>
    </Layout>
  );
}