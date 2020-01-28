
import { red } from '@material-ui/core/colors';
import { createMuiTheme, responsiveFontSizes } from '@material-ui/core/styles';

const theme = responsiveFontSizes(
  createMuiTheme({
    palette: {
      primary: { main: '#1976D2', contrastText: '#ffffff' },
      secondary: { main: '#AED581', contrastText: '#004D40' },
      error: red,
    },
    typography: {
      fontFamily: [
        // 'Open Sans',
        'Roboto',
        'sans-serif'
      ].join(','),
      h1: {
        fontSize: "5rem",
      }
    },
    drawerWidth: '14rem',
  }),
  {});

export default theme;
