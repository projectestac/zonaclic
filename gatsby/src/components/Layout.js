import React from 'react';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import Hidden from '@material-ui/core/Hidden';
import Container from '@material-ui/core/Container';
import Drawer from '@material-ui/core/Drawer';
import SwipeableDrawer from '@material-ui/core/SwipeableDrawer';
import TopBar from './TopBar';
import DrawerPanel from './DrawerPanel';
import Breadcrumbs from './Breadcrumbs';
import Footer from './Footer';

const useStyles = makeStyles(theme => ({
  root: {
    display: 'flex',
  },
  wrapper: {
    flexGrow: 1,
    display: 'flex',
    flexDirection: 'column',
    minHeight: '100vh',
  },
  content: {
    flexGrow: 1,
    padding: theme.spacing(3),
    marginTop: theme.mixins.toolbar.minHeight,
  },
  drawer: {
    [theme.breakpoints.up('sm')]: {
      width: theme.drawerWidth,
      flexShrink: 0,
      whiteSpace: 'nowrap',
    }
  },
  topBar: {
    [theme.breakpoints.up('sm')]: {
      width: `calc(100% - ${theme.drawerWidth})`,
      marginLeft: theme.drawerWidth,
    }
  },
  breadcrumbs: {
    marginBottom: theme.spacing(4),
  },
  footer: {
    marginTop: theme.spacing(8),
  },
  drawerPaper: {
    width: theme.drawerWidth,
  }
}));

export default function Layout({ intl, slug, children }) {

  const classes = useStyles();
  const [drawerOpen, setDrawerOpen] = React.useState(false);
  const handleDrawerToggle = () => { setDrawerOpen(!drawerOpen); }
  const theme = useTheme();

  return (
    <div className={classes.root}>
      <CssBaseline />
      <header>
        <TopBar className={classes.topBar} {...{ intl, drawerOpen, handleDrawerToggle }} />
        <nav className={classes.drawer} aria-label="main sections">
          <Hidden smUp implementation="css">
            <SwipeableDrawer
              variant="temporary"
              anchor={theme.direction === 'rtl' ? 'right' : 'left'}
              open={drawerOpen}
              onOpen={() => { }}
              onClose={handleDrawerToggle}
              classes={{
                paper: classes.drawerPaper,
              }}
              ModalProps={{
                keepMounted: true, // Better open performance on mobile.
              }}
            >
              <DrawerPanel {...{ intl }} />
            </SwipeableDrawer>
          </Hidden>
          <Hidden xsDown implementation="css">
            <Drawer
              variant="permanent"
              classes={{
                paper: classes.drawerPaper,
              }}
              open
            >
              <DrawerPanel {...{ intl }} />
            </Drawer>
          </Hidden>
        </nav>
      </header>
      <div className={classes.wrapper}>
        <Container className={classes.content} maxWidth="md">
          {slug && <Breadcrumbs className={classes.breadcrumbs} {...{ slug, intl }} />}
          <main>{children}</main>
        </Container>
        <Footer className={classes.footer} {...{ intl }} />
      </div>
    </div>
  );
}
