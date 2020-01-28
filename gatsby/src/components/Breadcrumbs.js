import React from 'react';
import { graphql, useStaticQuery } from 'gatsby';
import { makeStyles } from '@material-ui/core/styles';
import { mergeClasses } from '../utils/misc';
import MUIBreadcrumbs from '@material-ui/core/Breadcrumbs';
import { Link } from 'gatsby-plugin-intl';
import { siteMetadata } from '../../gatsby-config';

const { specialPages } = siteMetadata;

const useStyles = makeStyles(theme => ({
  root: {
    fontFamily: 'monospace',
  },
}));

export default function Breadcrumbs({ slug, intl, ...props }) {

  const classes = mergeClasses(props, useStyles());
  const { messages, locale, defaultLocale } = intl;
  const siteTitle = messages['site-title'];
  const allSlugs = useStaticQuery(graphql`
    query {
      allMdx {
        edges {
          node {
            frontmatter {
              title
            }
            fields {
              slug
              lang
            }
          }
        }
      }
    }
  `).allMdx.edges.map(edge => edge.node);

  const getFragments = slug => slug.split('/').filter(s => s.length > 0);

  const getSlugTitle = (slug) => {
    if (specialPages.includes(slug))
      return intl.messages[slug];

    const pages = allSlugs.filter(({ fields }) => fields.slug === slug);
    const match = pages.find(({ fields }) => fields.lang === locale) || pages.find(({ fields }) => fields.lang === defaultLocale);
    return match ? match.frontmatter.title : getFragments(slug).slice(-1);
  }

  return (
    <MUIBreadcrumbs {...props} className={classes.root} aria-label="breadcrumb">
      <Link to="/">{siteTitle}</Link>
      {getFragments(slug).map((_fragment, n, cmp) => {
        const subSlug = `/${cmp.slice(0, n + 1).join('/')}/`;
        const title = getSlugTitle(subSlug);
        if (n < cmp.length - 1) {
          // Parent path
          return <Link to={subSlug} key={n}>{title}</Link>
        }
        else
          // Current page
          return <span key={n}>{title}</span>
      })}
    </MUIBreadcrumbs>
  );

}