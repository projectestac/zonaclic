import React from 'react';
import { graphql } from 'gatsby';
import { useIntl, Link } from 'gatsby-plugin-intl';
import Layout from '../components/Layout';
import SEO from '../components/SEO';
import { getAllVariants } from '../utils/node';
import RepoMain from '../components/repo/RepoMain';

const SLUG = '/repo/';

export default function Repo({ data, location }) {

  const intl = useIntl();
  const { locale: lang, messages } = intl;
  const title = messages['repo-title'];
  const description = messages['repo-description'];
  const alt = getAllVariants(SLUG, location, intl.locale);

  return (
    <Layout {...{ intl }}>
      <SEO {...{ lang, title, description, alt }} />
      <RepoMain {...{ intl }} />
    </Layout>
  );
}