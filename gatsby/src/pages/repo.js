import React, { useState, useMemo } from 'react';
import queryString from 'query-string';
import { useIntl } from 'gatsby-plugin-intl';
import Layout from '../components/Layout';
import RepoMain from '../components/repo/RepoMain';

const SLUG = '/repo/';

export default function Repo({ location }) {

  const intl = useIntl();
  const [act, setAct] = useState();
  useMemo(() => setAct(queryString.parse(location.search)['act']), [location]);

  // SEO defined in inner elements

  return (
    <Layout {...{ intl, slug: `${SLUG}${act ? `${act}/` : ''}` }}>
      <RepoMain {...{ intl, location, SLUG, act }} />
    </Layout>
  );
}