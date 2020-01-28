import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import { changeLocale } from 'gatsby-plugin-intl';
import { siteMetadata } from '../../gatsby-config';
import { mergeClasses } from '../utils/misc';

const { supportedLanguages, langNames } = siteMetadata;

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
        {supportedLanguages.map(lang => (
          <MenuItem key={lang} value={lang}>
            {langNames[lang]}
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
}
