import React, { useState } from 'react';
import { makeStyles } from "@material-ui/core/styles";
import { mergeClasses } from '../../utils/misc';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
import Paper from '@material-ui/core/Paper';
import Switch from '@material-ui/core/Switch'
import SEO from '../SEO';
import ShareButtons from '../ShareButtons';
import SelectProjects from './SelectProjects';
import PaginatedList from './PaginatedList';
import ScrollMosaic from './ScrollMosaic';

const useStyles = makeStyles(_theme => ({
  root: {
  },
  selectProjects: {
    marginTop: '1rem',
  },
}));

function RepoList({ intl, projects, filters, setFilters, setLoading, setError, SLUG, REPO_BASE, location, ...props }) {

  const { locale, messages } = intl;
  const classes = mergeClasses(props, useStyles());
  const [list, setList] = useState(true);
  const title = messages['repo-title'];
  const description = messages['repo-description'];
  const card = `/cards/repo/card-${locale}.jpg`;

  return (
    <div {...props} className={classes.root}>
      <SEO {...{ location, lang: locale, title, description, slug: SLUG, thumbnail: card }} />
      <Typography variant="h1">{messages['repo-title']}</Typography>
      <ShareButtons {...{ intl, link: location?.href, title, description, slug: SLUG, thumbnail: card }} />
      <Paper className={classes['selectProjects']}>
        <SelectProjects {...{ intl, filters, setFilters, setLoading, setError }} />
      </Paper>
      <Divider />
      <Switch checked={list} onChange={_ev => setList(!list)} />
      {(list && <PaginatedList {...{ intl, projects, REPO_BASE }} />)
        || <ScrollMosaic {...{ intl, projects, REPO_BASE }} />
      }
    </div >
  );
}

export default RepoList;