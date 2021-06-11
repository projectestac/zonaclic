
import { createMuiTheme, responsiveFontSizes } from '@material-ui/core/styles';

//const mainFont = ['"Open Sans"', 'sans-serif'].join(',');
const mainFont = ['Roboto', 'sans-serif'].join(',');
const titleFont = ['"Open Sans"', 'sans-serif'].join(',');

const theme = responsiveFontSizes(
  createMuiTheme({
    palette: {
      // primary: { main: '#1976D2', contrastText: '#ffffff' },
      primary: { main: '#1976d2' },
      //secondary: { main: '#AED581', contrastText: '#004D40' },
      secondary: { main: '#fbc02d' },
      //error: red,
    },
    typography: {
      fontFamily: mainFont,
      //fontFamily: ['"Roboto"', 'sans-serif'].join(','),
      //fontFamily: ['"Lato"', 'sans-serif'].join(','),
      fontDisplay: 'swap',
      h1: {
        fontFamily: titleFont,
        //fontFamily: ['"Montserrat"', 'sans-serif'].join(','),
        fontWeight: 800,
        fontSize: '3.5rem',
        marginBottom: '1rem',
      },
      h2: {
        fontFamily: titleFont,
        //fontFamily: ['"Montserrat"', 'sans-serif'].join(','),
        fontWeight: 800,
        fontSize: '2.5rem',
        marginBottom: '0.8rem',
      },
      h3: {
        fontFamily: titleFont,
        //fontFamily: ['"Montserrat"', 'sans-serif'].join(','),
        fontWeight: 800,
        fontSize: '2rem',
        marginBottom: '0.6rem',
      },
      body3: {
        fontFamily: mainFont,
        //fontFamily: ['"Roboto"', 'sans-serif'].join(','),
        fontWeight: 400,
        fontSize: '1rem',
        lineHeight: 1.5,
        letterSpacing: '0.00938em',
      },
    },
    drawerWidth: '14rem',
  }),
  {});

export default theme;
