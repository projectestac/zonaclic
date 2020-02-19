import React, { useState, useEffect } from 'react';
import { makeStyles } from "@material-ui/core/styles";
import { mergeClasses, checkFetchResponse } from '../../utils/misc';
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
  const [fullProjectList, setFullProjectList] = useState(null);
  const [projects, setProjects] = useState(null);
  const [project, setProject] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filters, setFilters] = useState({ language: '', subject: '', level: '', text: '', textMatches: [] });

  const loadFullProjectList = () => fetch(`${REPO_BASE}${REPO_LIST}`, { referrerPolicy: 'no-referrer' })
    .then(checkFetchResponse)
    .then(_fullList => setFullProjectList(_fullList))
    .catch(err => setError(err?.toString() || 'Error'));

  useEffect(() => {
    // Clear previous states
    setError(null);
    // Load the given project
    if (act && (project === null || project.path !== act)) {
      setLoading(true);
      setProject(null);
      setProjects(null);
      const fullPath = `${REPO_BASE}${act}`;
      // Load a specific project
      fetch(`${fullPath}/project.json`, { referrerPolicy: 'no-referrer' })
        .then(checkFetchResponse)
        .then(_project => {
          _project.path = act;
          _project.fullPath = fullPath;
          setProject(_project);
          setLoading(false);
          if (_project.relatedTo && !fullProjectList)
            loadFullProjectList();
        })
        .catch(err => {
          setError(err?.toString() || 'Error');
        });
    } else if (!act) {
      setLoading(true);
      setProject(null);
      if (fullProjectList) {
        setProjects(fullProjectList.filter(prj => (
          !filters.language || prj?.langCodes?.includes(filters.language))
          && (!filters.subject || prj?.areaCodes?.includes(filters.subject))
          && (!filters.level || prj?.levelCodes?.includes(filters.level))
          && (!filters.text || filters?.textMatches?.includes(prj.path))));
        setLoading(false);
      }
      else
        loadFullProjectList();
    }
  }, [act, project, fullProjectList, filters]);

  return (
    <div {...props} className={classes.root}>
      {
        (error && <h2>{formatMessage({ id: 'error' }, { error })}</h2>) ||
        (loading && <Loading {...{ intl }} />) ||
        (project && <Project {...{ intl, project, SLUG, REPO_BASE, location, fullProjectList }} />) ||
        (projects && <RepoList {...{ intl, projects, filters, setFilters, setLoading, setError, SLUG, REPO_BASE, location }} />)
      }
    </div>
  );
}

export default RepoMain;