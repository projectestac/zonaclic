import React from 'react';
import { useStaticQuery, graphql, withPrefix } from 'gatsby';
import { Link, navigate } from 'gatsby-plugin-intl';
import { makeStyles } from '@material-ui/core/styles';
import Toolbar from '@material-ui/core/Toolbar';
import Divider from '@material-ui/core/Divider';
import Typography from '@material-ui/core/Typography';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import { getAllResolvedVersionsForLanguage } from '../utils/node';
import { FontAwIcon } from '../utils/FontAwIcon';
import { mergeClasses } from '../utils/misc';

const useStyles = makeStyles(theme => ({
  toolbar: theme.mixins.toolbar,
  title: {
    flexGrow: 1,
    '& a': {
      color: 'inherit',
      textDecoration: 'none',
    },
  },
  logo: {
    maxHeight: '32px',
    margin: theme.spacing(0, 1),
  },
  listItemIcon: {
    minWidth: '2rem',
  },
}));

const query = graphql`
  query {
    site {
      siteMetadata {
        supportedLanguages
      }
    }
    allMdx(filter: {fields: {slug: {regex: "/^(?!\\/blog\\/)/"}}}, sort: {fields: frontmatter___order, order: ASC}) {
      edges {
        node {
          excerpt
          fields {
            lang
            slug
          }
          frontmatter {
            title
            order
            date
            description
            icon
          }
        }
      }
    }
  }
`;

export default function DrawerPanel({ intl, ...props }) {

  const data = useStaticQuery(query);
  const pages = getAllResolvedVersionsForLanguage(data, intl);
  const classes = mergeClasses(props, useStyles());

  return (
    <div {...props}>
      <Toolbar className={classes.toolbar} disableGutters>
        <Link to='/'><img className={classes.logo} src={withPrefix('/icons/icon-48x48.png')} alt="logo" /></Link>
        <Typography variant="h6" noWrap className={classes.title}>
          {intl.messages['site-title']}
        </Typography>
      </Toolbar>
      <Divider />
      <List>
        {pages.map(node => (
          <ListItem button key={node.fields.slug} onClick={() => navigate(node.fields.slug)}>
            <ListItemIcon className={classes.listItemIcon}><FontAwIcon icon={node.frontmatter.icon} size="lg" /></ListItemIcon>
            <ListItemText primary={node.frontmatter.title} />
          </ListItem>
        ))}
      </List>
      <Divider />
      <ListItem button onClick={() => navigate('/blog/')}>
        <ListItemIcon className={classes.listItemIcon}><FontAwIcon icon="rss-square" size="lg" /></ListItemIcon>
        <ListItemText primary="Blog" />
      </ListItem>
    </div>
  );
}