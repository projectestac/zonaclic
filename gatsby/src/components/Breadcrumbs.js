import React from 'react';
import { graphql, useStaticQuery } from 'gatsby';
import { makeStyles } from '@material-ui/core/styles';
import { mergeClasses } from '../utils/misc';
import MUIBreadcrumbs from '@material-ui/core/Breadcrumbs';
import { Link } from 'gatsby-plugin-intl';

const query = graphql`
  query {
    site {
      siteMetadata {
        specialPages
      }
    }
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
`;

const useStyles = makeStyles(_theme => ({
  root: {
    fontFamily: 'monospace',
  },
}));

export default function Breadcrumbs({ slug, intl, ...props }) {

  const classes = mergeClasses(props, useStyles());
  const { messages, locale, defaultLocale } = intl;
  const siteTitle = messages['site-title'];
  const data = useStaticQuery(query);
  const { site: { siteMetadata: { specialPages } } } = data;
  const allSlugs = data.allMdx.edges.map(edge => edge.node);

  const getFragments = slug => slug.split('/').filter(s => s.length > 0);

  const getSlugTitle = (slug) => {
    if (specialPages.includes(slug))
      return intl.messages[slug];

    const pages = allSlugs.filter(({ fields }) => fields.slug === slug);
    const match = pages.find(({ fields }) => fields.lang === locale) || pages.find(({ fields }) => fields.lang === defaultLocale);
    return match ? match.frontmatter.title : getFragments(slug).slice(-1);
  }

  let noMoreLinks = false;
  let linkAsUser = false;

  return (
    <MUIBreadcrumbs {...props} className={classes.root} aria-label="breadcrumb">
      <Link to="/">{siteTitle}</Link>
      {getFragments(slug).map((_fragment, n, cmp) => {
        let subSlug = `/${cmp.slice(0, n + 1).join('/')}/`;
        const title = getSlugTitle(subSlug);
        if (!noMoreLinks && n < cmp.length - 1) {
          // Parent path

          // Check special cases
          if (subSlug === '/repo/')
            // inner path fragments to activities should not be linked
            noMoreLinks = true;
          else if (linkAsUser)
            // user name should be passed as parameter, not as a path fragment
            subSlug = subSlug.replace(/\/([\w.-]+)\/$/, '/?user=$1');
          else if (subSlug === '/user/')
            linkAsUser = true;

          return <Link to={subSlug} key={n}>{title}</Link>
        }
        else
          // Current page
          return <span key={n}>{title}</span>
      })}
    </MUIBreadcrumbs>
  );

}
