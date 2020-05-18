import React from 'react';
import { useStaticQuery, graphql } from 'gatsby';
import { Link } from 'gatsby-plugin-intl';
import Grid from "@material-ui/core/Grid";
import Typography from "@material-ui/core/Typography";
import { makeStyles } from "@material-ui/core/styles";
import { mergeClasses, isAbsoluteUrl } from '../utils/misc';
import Container from "@material-ui/core/Container";
import Box from "@material-ui/core/Box";
import footerData from "../../content/footer.json";

const useStyles = makeStyles(theme => ({
  root: {
    backgroundColor: theme.palette.grey[300],
    borderTop: `1px solid ${theme.palette.divider}`,
    paddingTop: theme.spacing(3),
    paddingBottom: theme.spacing(3),
    [theme.breakpoints.up(`sm`)]: {
      paddingTop: theme.spacing(6),
      paddingBottom: theme.spacing(6),
    },
  },
  blockList: {
    listStyleType: 'none',
    paddingInlineStart: 'inherit',
  }
}));

const query = graphql`
  query InfoQuery {
    logo: file(absolutePath: { regex: "/mendeleev-2\\.jpg/" }) {
      childImageSharp {
        fixed(width: 50, height: 50) {
          ...GatsbyImageSharpFixed
        }
      }
    }
    site {
      siteMetadata {
        author
        description
        version
        defaultLanguage
      }
    }
  }
`;


function Footer({ intl: { locale, messages }, ...props }) {

  const data = useStaticQuery(query);
  const blocks = footerData[locale] || footerData[data.site.siteMetadata.defaultLanguage];
  const classes = mergeClasses(props, useStyles());

  return (
    <Container {...props} component="footer" className={classes.root} maxWidth={false}>
      <Container maxWidth="lg">
        <Grid container spacing={4} justify="space-evenly">
          {blocks.map((block, n) => (
            <Grid item xs={6} md={3} key={n}>
              <Typography variant="h6" color="textPrimary" gutterBottom>
                {block.title}
              </Typography>
              <ul className={classes.blockList}>
                {block.items.map(({ name, link }, k) => (
                  <li key={k}>
                    {
                      isAbsoluteUrl(link) ?
                        <a href={link} target="_blank" rel="noopener noreferrer">{name}</a> :
                        <Link to={link}>{name}</Link>
                    }
                  </li>
                ))}
              </ul>
            </Grid>
          ))}
        </Grid>
        <Box mt={5} display="flex" flexDirection="row" justifyContent="space-around">
          <Typography variant="body2" color="textSecondary" align="center">
            {messages['site-description']}<br />{`v. ${data.site.siteMetadata.version}`}
          </Typography>
        </Box>
      </Container>
    </Container>
  );
}

export default Footer;