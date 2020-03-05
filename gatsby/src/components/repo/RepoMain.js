import React, { useState, useEffect } from 'react';
import { checkFetchResponse } from '../../utils/misc';
import Project from './Project';
import RepoList from './RepoList';
import Loading from './Loading';


function RepoMain({ location, SLUG, intl, repoBase, repoList, jnlpInstaller, act }) {

  const { formatMessage } = intl;
  const [fullProjectList, setFullProjectList] = useState(null);
  const [projects, setProjects] = useState(null);
  const [project, setProject] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filters, setFilters] = useState({ language: '', subject: '', level: '', text: '', textMatches: [] });
  const [listMode, setListMode] = useState(false);

  // Update `fullProjectList`, `projects` and `project`
  useEffect(() => {

    function loadFullProjectList() {
      return fetch(repoList, { referrerPolicy: 'no-referrer' })
        .then(checkFetchResponse)
        .then(_fullList => setFullProjectList(_fullList))
        .catch(err => setError(err?.toString() || 'Error'));
    }

    // Clear previous states
    setError(null);
    // Load the given project
    if (act && (project === null || project.path !== act)) {
      setLoading(true);
      setProject(null);
      setProjects(null);
      const fullPath = `${repoBase}/${act}`;
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
  }, [repoBase, repoList, act, project, fullProjectList, filters]);

  return (
    (error && <h2>{formatMessage({ id: 'error' }, { error })}</h2>) ||
    (loading && <Loading {...{ intl }} />) ||
    (project && <Project {...{ intl, project, SLUG, location, fullProjectList, jnlpInstaller }} />) ||
    (projects && <RepoList {...{ intl, repoBase, projects, filters, setFilters, listMode, setListMode, setLoading, setError, SLUG, location }} />)
  );
}

export default RepoMain;