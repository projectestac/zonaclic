import React from 'react';
import { graphql } from 'gatsby';
import { MDXRenderer } from 'gatsby-plugin-mdx';
import { navigate, useIntl } from "gatsby-plugin-intl"
import Layout from '../components/Layout';
import SEO from '../components/SEO';
import { dateFormat } from '../utils/defaults';
import { getResolvedVersionForLanguage, getAllVersions } from '../utils/node';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import ArrowBackIcon from '@material-ui/icons/ChevronLeft';
import ArrowForwardIcon from '@material-ui/icons/ChevronRight';

const useStyles = makeStyles(theme => ({
  article: {
    ...theme.typography.body1,
    '& h1': { ...theme.typography.h1 },
    '& h2': { ...theme.typography.h2 },
    '& h3': { ...theme.typography.h3 },
    '& h4': { ...theme.typography.h4 },
    '& h5': { ...theme.typography.h5 },
    '& h6': { ...theme.typography.h6 },
    '& code[class*="language-"], pre[class*="language-"]': {
      fontSize: '0.9em',
    }
  },
  blogNav: {
    display: 'grid',
    gridTemplateColumns: '1fr 1fr',
    gridGap: theme.spacing(2),
    marginTop: theme.spacing(4),
    paddingTop: theme.spacing(4),
    borderTop: `1px solid ${theme.palette.common.black}`,
  },
}));

export default function BlogPostTemplate({ data, pageContext, location }) {

  const classes = useStyles();
  const intl = useIntl();
  const { locale: lang, formatDate } = intl;
  const { frontmatter, excerpt, body, fields: { slug } } = getResolvedVersionForLanguage(data, intl);
  const { title, description = excerpt, date } = frontmatter;
  const { previous, next } = pageContext;
  const alt = getAllVersions(data, location, lang);

  return (
    <Layout {...{ intl, slug }}>
      <SEO {...{ lang, title, slug, alt, description }} />
      <article className={classes.article}>
        <header>
          <h1>{title}</h1>
          <p>{formatDate(date, dateFormat)}</p>
        </header>
        <MDXRenderer {...{ frontmatter, intl }}>{body}</MDXRenderer>
      </article>
      <nav className={classes.blogNav}>
        {previous ? (
          <Button size="small" variant="outlined" onClick={() => navigate(previous.fields.slug)}>
            <ArrowBackIcon />
            <div className="text-with-ellipsis">{previous.frontmatter.title}</div>
          </Button>
        ) : <div />}
        {next ? (
          <Button size="small" variant="outlined" onClick={() => navigate(next.fields.slug)}>
            <div className="text-with-ellipsis">{next.frontmatter.title}</div>
            <ArrowForwardIcon />
          </Button>
        ) : <div />}
      </nav>
    </Layout>
  );
}

export const pageQuery = graphql`
  query PostsBySlug($slug: String!) {
    allMdx(filter: {fields: {slug: {eq: $slug}}}) {
      edges {
        node {
          id
          body
          fields {
            lang
            slug
          }
          frontmatter {
            date
            description
            title
          }
          excerpt(pruneLength: 160)
        }
      }
    }
  }
`;
