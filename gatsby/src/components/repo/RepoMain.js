import React, { useState, useEffect } from 'react';
import Typography from "@material-ui/core/Typography";
import { makeStyles } from "@material-ui/core/styles";
import { mergeClasses } from '../../utils/misc';
import Container from "@material-ui/core/Container";
import CircularProgress from '@material-ui/core/CircularProgress';
import Project from './Project';
import RepoList from "./RepoList";


const REPO_BASE = 'https://clic.xtec.cat/projects/';
const REPO_LIST = 'projects.json';

const useStyles = makeStyles(_theme => ({
  root: {
  },
}));

function RepoMain({ location, SLUG, intl, act, ...props }) {

  const classes = mergeClasses(props, useStyles());
  const { messages, formatMessage } = intl;
  const [projects, setProjects] = useState([]);
  const [project, setProject] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    if (act) {
      const fullPath = `${REPO_BASE}${act}`;
      // Load a specific project
      fetch(`${fullPath}/project.json`)
        .then(response => {
          if (!response.ok)
            throw new Error(`Network error: ${response.statusText}`);
          return response.json();
        })
        .then(_project => {
          _project.path = act;
          _project.fullPath = fullPath;
          setProject(_project);
          setLoading(false);
        })
        .catch(err => {
          setError(err);
        });
    } else {
      // Load the full repo list
      fetch(`${REPO_BASE}${REPO_LIST}`)
        .then(response => {
          if (!response.ok)
            throw new Error(`Network error: ${response.statusText}`);
          return response.json();
        })
        .then(_projects => {
          setProjects(_projects);
          setLoading(false);
        })
        .catch(err => {
          setError(err);
        });
    }
  }, [location.search, act]);

  return (
    <Container {...props} className={classes.root}>
      <Typography variant="h2">{messages['repo-title']}</Typography>
      {
        (loading && <CircularProgress />) ||
        (error && <h2>{formatMessage({ id: 'error' }, { error })}</h2>) ||
        (act && <Project {...{ project, intl, SLUG, location }} />) ||
        <RepoList {...{ projects, intl, SLUG, location }} />
      }
    </Container>
  );
}

export default RepoMain;