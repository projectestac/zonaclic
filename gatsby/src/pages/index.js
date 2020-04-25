import React from 'react';
import { graphql } from 'gatsby';
import { useIntl, Link } from 'gatsby-plugin-intl';
import Layout from '../components/Layout';
import SEO from '../components/SEO';
import FrontItem from '../components/FrontItem';
import { getAllResolvedVersionsForLanguage } from '../utils/node';

const SLUG = '/';

export default function Index({ data, location }) {

  const intl = useIntl();
  const { locale: lang, messages } = intl;
  const title = messages['site-title'];
  const description = messages['site-description'];
  const pages = getAllResolvedVersionsForLanguage(data, intl);

  return (
    <Layout {...{ intl }}>
      <SEO {...{ lang, title, description, location, slug: SLUG }} />
      <h2>{messages['pages']}</h2>
      {pages.map(node => (
        <FrontItem node={node} key={node.fields.slug} />
      ))}
      <hr />
      <h2><Link to="/blog/">Blog</Link></h2>
    </Layout>
  );
}

export const pageQuery = graphql`
  query {
    site {
      siteMetadata {
        supportedLanguages
      }
    }
    allMdx(filter: {frontmatter: {drawer_pos: {gt: 0}}}, sort: {fields: frontmatter___drawer_pos}) {
      edges {
        node {
          excerpt
          fields {
            lang
            slug
          }
          frontmatter {
            title
            date
            description
          }
        }
      }
    }
  }
`;
