import React, { useState, useMemo } from 'react';
import { graphql } from 'gatsby';
import queryString from 'query-string';
import { useIntl } from 'gatsby-plugin-intl';
import { makeStyles } from '@material-ui/core/styles';
import { getResolvedVersionForLanguage } from '../utils/node';
import Layout from '../components/Layout';
import UserMain from '../components/user/UserMain';

const SLUG = '/user/';

const useStyles = makeStyles(theme => ({
  root: {
    '& h1': { ...theme.typography.h1, color: theme.palette.primary.dark, },
    '& h2': { ...theme.typography.h2, color: theme.palette.primary.dark, },
  },
}));

export default function User({ data, location }) {

  const { site: { siteMetadata: { usersBase, googleOAuth2Id, jnlpInstaller, userLibApi} } } = data;
  const classes = useStyles();
  const intl = useIntl();
  const userLibInfoNode = getResolvedVersionForLanguage(data, intl);
  const [user, setUser] = useState();
  useMemo(() => setUser(queryString.parse(location.search)['user']), [location]);
  const [act, setAct] = useState();
  useMemo(() => setAct(queryString.parse(location.search)['act']), [location]);

  // SEO defined in inner elements

  return (
    <Layout {...{ intl, slug: `${SLUG}${user ? `${user}/` : ''}${act ? `${act}/` : ''}` }}>
      <article className={classes.root}>
        <UserMain {...{ intl, usersBase, location, SLUG, googleOAuth2Id, userLibApi, jnlpInstaller, userLibInfoNode, user, act }} />
      </article>
    </Layout>
  );
}

export const pageQuery = graphql`
  query {
    site {
      siteMetadata {
        usersBase
        googleOAuth2Id
        userLibApi
        jnlpInstaller
      }
    }
    allMdx(filter: {fields: {slug: {eq: "/userlib/"}}}) {
      edges {
        node {
          id
          body
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
`;

export const userSlug = SLUG;
