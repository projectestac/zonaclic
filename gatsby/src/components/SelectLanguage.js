import React from 'react';
import { useStaticQuery, graphql } from 'gatsby';
import { makeStyles } from '@material-ui/core/styles';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import { changeLocale } from 'gatsby-plugin-intl';
import { mergeClasses } from '../utils/misc';

const query = graphql`
  query {
    site {
      siteMetadata {
        langNames
        supportedLanguages
      }
    }
  }
`;

const useStyles = makeStyles(theme => ({
  root: {
    minWidth: '4rem',
  },
  select: {
    paddingTop: theme.spacing(1),
    paddingBottom: theme.spacing(1),
    color: theme.palette.primary.contrastText,
  },
  icon: {
    color: theme.palette.primary.contrastText,
    borderColor: theme.palette.primary.contrastText,
  },
}));

export default function ({ intl: { locale, messages }, ...props }) {

  const classes = mergeClasses(props, useStyles());
  const { site: { siteMetadata: { supportedLanguages, langNames } } } = useStaticQuery(query);
  const handleChange = ev => changeLocale(ev.target.value);

  return (
    <FormControl
      {...props}
      className={classes.root}
      variant="outlined"
      title={messages['change-language']}
    >
      <Select
        classes={{ root: classes.select, icon: classes.icon }}
        value={locale}
        onChange={handleChange}
        renderValue={value => value}
      >
        {supportedLanguages.map((lang, n) => (
          <MenuItem key={lang} value={lang}>
            {langNames[n]}
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
}
