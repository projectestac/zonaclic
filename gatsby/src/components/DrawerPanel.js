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
  const { messages } = intl;
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
        <ListItem button onClick={() => navigate('/repo/')}>
          <ListItemIcon className={classes.listItemIcon}><FontAwIcon icon="cubes" size="lg" /></ListItemIcon>
          <ListItemText primary={messages['repo-title']} />
        </ListItem>
        <ListItem button onClick={() => navigate('/user/')}>
          <ListItemIcon className={classes.listItemIcon}><FontAwIcon icon="users" size="lg" /></ListItemIcon>
          <ListItemText primary={messages['user-repo']} />
        </ListItem>
        {pages.map(({ fields: { slug }, frontmatter: { icon, title } }) => (
          <ListItem button key={slug} onClick={() => navigate(slug)}>
            <ListItemIcon className={classes.listItemIcon}><FontAwIcon icon={icon} size="lg" /></ListItemIcon>
            <ListItemText primary={title} />
          </ListItem>
        ))}
      </List>
      <Divider />
      <ListItem button onClick={() => navigate('/blog/')}>
        <ListItemIcon className={classes.listItemIcon}><FontAwIcon icon="rss-square" size="lg" /></ListItemIcon>
        <ListItemText primary={messages['blog-index-title']} />
      </ListItem>
    </div>
  );
}