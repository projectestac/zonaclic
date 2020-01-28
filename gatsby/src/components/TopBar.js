import React from 'react';
import clsx from 'clsx';
import { makeStyles } from '@material-ui/core/styles';
import Hidden from '@material-ui/core/Hidden';
import PropTypes from 'prop-types';
import { Link } from 'gatsby-plugin-intl';
import useScrollTrigger from '@material-ui/core/useScrollTrigger';
import Slide from '@material-ui/core/Slide';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import SearchIcon from '@material-ui/icons/Search';
import SelectLanguage from './SelectLanguage';
import SearchBox from './SearchBox';
import { mergeClasses } from '../utils/misc';

const useStyles = makeStyles(theme => ({
  appBar: {
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
    [theme.breakpoints.up('sm')]: {
      width: `calc(100% - ${theme.drawerWidth})`,
      marginLeft: theme.drawerWidth,
    },
  },
  searchBar: {
    backgroundColor: theme.palette.primary.dark,
  },
  menuButton: {
    [theme.breakpoints.up('sm')]: {
      display: 'none',
    },
  },
  hide: {
    display: 'none',
  },
  toolbar: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    padding: theme.spacing(0, 1),
    ...theme.mixins.toolbar,
  },
  title: {
    flexGrow: 1,
    '& a': {
      color: 'inherit',
      textDecoration: 'none',
    },
  },
}));

function HideOnScroll({ children }) {
  const trigger = useScrollTrigger();
  return (
    <Slide appear={false} direction="down" in={!trigger}>
      {children}
    </Slide>
  );
}

HideOnScroll.propTypes = {
  children: PropTypes.element.isRequired,
};

function TopBar({ intl, drawerOpen, handleDrawerToggle, ...props }) {

  const classes = mergeClasses(props, useStyles());
  const [searchBarOpen, setSearchBarOpen] = React.useState(false);
  const handleSearchBarToggle = () => { setSearchBarOpen(!searchBarOpen); }

  return (
    <HideOnScroll {...props}>
      <AppBar
        position="fixed"
        className={classes.appBar}
      >
        <Toolbar>
          <IconButton
            color="inherit"
            aria-label="open drawer"
            onClick={handleDrawerToggle}
            edge="start"
            className={clsx(classes.menuButton, { [classes.hide]: drawerOpen })}
          >
            <MenuIcon />
          </IconButton>
          <Typography variant="h6" noWrap className={classes.title}>
            <Link to='/'>{intl.messages['site-title']}</Link>
          </Typography>
          <Hidden implementation="css" smDown>
            <SearchBox {...{ intl }} />
          </Hidden>
          <Hidden implementation="css" mdUp>
            <IconButton aria-label="search" color="inherit" onClick={handleSearchBarToggle}>
              <SearchIcon />
            </IconButton>
          </Hidden>
          <SelectLanguage {...{ intl }} />
        </Toolbar>
        {searchBarOpen &&
          <Toolbar className={classes.searchBar}>
            <SearchBox {...{ intl }} />
          </Toolbar>
        }
      </AppBar>
    </HideOnScroll>
  );
}

export default TopBar;