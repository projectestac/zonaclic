import React, { useState, useEffect } from 'react';
import { makeStyles } from "@material-ui/core/styles";
import { mergeClasses } from '../../utils/misc';
import Project from './Project';
import RepoList from './RepoList';
import Loading from './Loading';

const REPO_BASE = 'https://clic.xtec.cat/projects/';
const REPO_LIST = 'projects.json';

const useStyles = makeStyles(_theme => ({
  root: {
  },
}));

function RepoMain({ location, SLUG, intl, act, ...props }) {

  const classes = mergeClasses(props, useStyles());
  const { formatMessage } = intl;
  const [projects, setProjects] = useState(null);
  const [project, setProject] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Clear previous states
    setProjects(null);
    setProject(null);
    setError(null);
    // Load the given project
    if (act) {
      const fullPath = `${REPO_BASE}${act}`;
      // Load a specific project
      fetch(`${fullPath}/project.json`, { referrerPolicy: 'no-referrer' })
        .then(response => {
          if (!response.ok)
            throw new Error(response.statusText);
          return response.json();
        })
        .then(_project => {
          _project.path = act;
          _project.fullPath = fullPath;
          setProject(_project);
        })
        .catch(err => {
          setError(err?.toString() || 'Error');
        });
    } else {
      // Load the full repo list
      fetch(`${REPO_BASE}${REPO_LIST}`, { referrerPolicy: 'no-referrer' })
        .then(response => {
          if (!response.ok)
            throw new Error(response.statusText);
          return response.json();
        })
        .then(_projects => {
          setProjects(_projects);
        })
        .catch(err => {
          setError(err?.toString() || 'Error');
        });
    }
  }, [act]);

  return (
    <div {...props} className={classes.root}>
      {
        (error && <h2>{formatMessage({ id: 'error' }, { error })}</h2>) ||
        (project && <Project {...{ project, intl, SLUG, REPO_BASE, location }} />) ||
        (projects && <RepoList {...{ projects, intl, SLUG, REPO_BASE, location }} />) ||
        <Loading {...{ intl }} />
      }
    </div>
  );
}

export default RepoMain;