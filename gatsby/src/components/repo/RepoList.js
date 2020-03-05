import React, { createRef } from 'react';
import { makeStyles } from "@material-ui/core/styles";
import { mergeClasses } from '../../utils/misc';
import Typography from '@material-ui/core/Typography';
import Paper from '@material-ui/core/Paper';
import SEO from '../SEO';
import ShareButtons from '../ShareButtons';
import SelectProjects from './SelectProjects';
import PaginatedList from './PaginatedList';
import ScrollMosaic from './ScrollMosaic';
import ToggleButtonGroup from '@material-ui/lab/ToggleButtonGroup';
import ToggleButton from '@material-ui/lab/ToggleButton';
import ViewListIcon from '@material-ui/icons/List';
import ViewCardsIcon from '@material-ui/icons/ViewComfy';

const useStyles = makeStyles(_theme => ({
  root: {
  },
  selectProjects: {
    marginTop: '1rem',
    marginBottom: '1rem',
  },
  infoBar: {
    display: 'flex',
    alignItems: 'flex-end',
    marginTop: '1rem',
    marginBottom: '1rem',
  },
  viewMode: {
    flexGrow: 1,
    background: 'transparent',
  }
}));

function RepoList({ intl, repoBase, projects, filters, setFilters, listMode, setListMode, setLoading, setError, SLUG, location, ...props }) {

  const { locale, messages, formatNumber, formatMessage } = intl;
  const classes = mergeClasses(props, useStyles());
  const title = messages['repo-title'];
  const description = messages['repo-description'];
  const card = `/cards/repo/card-${locale}.jpg`;
  const projectCount = formatMessage(
    { id: projects.length === 1 ? 'repo-num-single' : 'repo-num-plural' },
    { num: formatNumber(projects.length) });
  const topRef = createRef();

  return (
    <div {...props} className={classes.root}>
      <SEO {...{ location, lang: locale, title, description, slug: SLUG, thumbnail: card }} />
      <Typography variant="h1" ref={topRef}>{messages['repo-title']}</Typography>
      <ShareButtons {...{ intl, link: location?.href, title, description, slug: SLUG, thumbnail: card }} />
      <Paper className={classes['selectProjects']}>
        <SelectProjects {...{ intl, filters, setFilters, setLoading, setError }} />
      </Paper>
      <div className={classes['infoBar']}>
        <ToggleButtonGroup
          className={classes['viewMode']}
          size="small"
          value={listMode}
          exclusive
          onChange={_ev => setListMode(!listMode)}
          aria-label={messages['repo-view-mode']}
        >
          <ToggleButton value={false} title={messages['repo-view-cards']}>
            <ViewCardsIcon />
          </ToggleButton>
          <ToggleButton value={true} title={messages['repo-view-list']}>
            <ViewListIcon />
          </ToggleButton>
        </ToggleButtonGroup>
        <Typography variant="body2">{projectCount}</Typography>
      </div>
      {(listMode && <PaginatedList {...{ intl, SLUG, repoBase, projects }} />)
        || <ScrollMosaic {...{ intl, SLUG, repoBase, projects, topRef }} />
      }
    </div >
  );
}

export default RepoList;