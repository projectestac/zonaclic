import React, { useState } from 'react';
import { makeStyles } from "@material-ui/core/styles";
import { mergeClasses, checkFetchResponse } from '../../utils/misc';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import TextField from '@material-ui/core/TextField';
import InputAdornment from '@material-ui/core/InputAdornment';
import IconButton from '@material-ui/core/IconButton';
import SearchIcon from '@material-ui/icons/Search';

const JCLIC_SEARCH_SERVICE = 'https://clic.xtec.cat/db/repo-search/index.php';

const useStyles = makeStyles(theme => ({
  root: {
    display: 'flex',
    flexWrap: 'wrap',
  },
  formControl: {
    margin: theme.spacing(1),
    width: '10rem',
  },
}));

function SelectProjects({ intl, filters, setFilters, setLoading, setError, ...props }) {

  const classes = mergeClasses(props, useStyles());
  const { messages, locale } = intl;
  const [query, setQuery] = useState(filters.text);
  const handleChange = ev => {
    ev.preventDefault();
    const { target: { name, value } } = ev;
    setFilters({ ...filters, [name]: value === '*' ? '' : value })
  }
  const handleEnterSearch = ev => {
    if (ev.type === 'click' || ev.key === 'Enter') {
      ev.preventDefault();
      if (query) {
        setLoading(true);
        fetch(`${JCLIC_SEARCH_SERVICE}?lang=${locale}&method=boolean&q=${encodeURIComponent(query)}`)
          .then(checkFetchResponse)
          .then(textMatches => {
            setFilters({ ...filters, text: query, textMatches });
          })
          .catch(err => setError(err?.toString() || 'Error'));
      }
      else
        setFilters({ ...filters, text: '', textMatches: [] });
    }
  }

  return (
    <div className={classes.root}>
      <FormControl className={classes.formControl}>
        <InputLabel id="select-lang-label">{messages['prj-language']}</InputLabel>
        <Select
          labelId="select-lang-label"
          name="language"
          value={filters.language || ''}
          onChange={handleChange}>
          {messages['lang-codes'].split('|').map((code) => <MenuItem key={code} value={code}>{messages[`lang-${code}`]}</MenuItem>)}
        </Select>
      </FormControl>
      <FormControl className={classes.formControl}>
        <InputLabel id="select-subj-label">{messages['prj-subject']}</InputLabel>
        <Select
          labelId="select-subj-label"
          name="subject"
          value={filters.subject || ''}
          onChange={handleChange}>
          {messages['subj-codes'].split('|').map((code) => <MenuItem key={code} value={code}>{messages[`subj-${code}`]}</MenuItem>)}
        </Select>
      </FormControl>
      <FormControl className={classes.formControl}>
        <InputLabel id="select-level-label">{messages['prj-level']}</InputLabel>
        <Select
          labelId="select-level-label"
          name="level"
          value={filters.level || ''}
          onChange={handleChange}>
          {messages['level-codes'].split('|').map((code) => <MenuItem key={code} value={code}>{messages[`level-${code}`]}</MenuItem>)}
        </Select>
      </FormControl>
      <FormControl className={classes.formControl}>
        <TextField
          label={messages['prj-text']}
          value={query}
          onChange={({ target: { value } }) => setQuery(value)}
          onKeyPress={handleEnterSearch}
          InputProps={{
            endAdornment:
              <InputAdornment position="end">
                <IconButton
                  aria-label={messages['search']}
                  title={messages['search']}
                  onClick={handleEnterSearch}
                  edge="end"
                >
                  <SearchIcon />
                </IconButton>
              </InputAdornment>
          }}
        />
      </FormControl>
    </div>
  );
}

export default SelectProjects;