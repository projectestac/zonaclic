
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
      fontFamily: ['Roboto', 'sans-serif'].join(','),
      h1: {
        fontFamily: ['Catamaran', 'sans-serif'].join(','),
        fontWeight: 800,
        opacity: 0.75,
        fontSize: '4rem',
        marginBottom: '1rem',
      },
      h2: {
        fontFamily: ['Catamaran', 'sans-serif'].join(','),
        fontWeight: 800,
        opacity: 0.75,
        fontSize: '2.5rem',
        marginBottom: '0.8rem',
      },
      h3: {
        fontFamily: ['Montserrat', 'sans-serif'].join(','),
        fontWeight: 800,
        opacity: 0.75,
        fontSize: '2rem',
        marginBottom: '0.6rem',
      },
      body2: {
        fontFamily: ['Merriweather', 'Georgia', 'serif'].join(','),
      },
    },
    drawerWidth: '14rem',
  }),
  {});

export default theme;
