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
import Typography from '@material-ui/core/Typography';

const useStyles = makeStyles(_theme => ({
  root: {
    display: 'flex',
    flexWrap: 'wrap',
    padding: '1rem 0.5rem',
  },
  label: {
    flexBasis: '100%',
    marginLeft: '0.5rem',
    marginBottom: '0.5rem',
  },
  formControl: {
    marginRight: '0.5rem',
    marginLeft: '0.5rem',
    width: '10rem',
    flexGrow: 1,
    maxWidth: '21rem',
  },
}));

function SelectProjects({ intl, jclicSearchService, filters, setFilters, setLoading, setError, ...props }) {

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
        fetch(`${jclicSearchService}?lang=${locale}&method=boolean&q=${encodeURIComponent(query)}`)
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
      <Typography color="textSecondary" className={classes['label']}>{messages['prj-filter']}</Typography>
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