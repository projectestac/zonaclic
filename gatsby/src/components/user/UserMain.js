import React, { useState, useEffect } from 'react';
import { checkFetchResponse } from '../../utils/misc';
import Project from '../repo/Project';
import RepoList from '../repo/RepoList';
import Loading from '../repo/Loading';
import UserLib from './UserLib';

function UserMain({ location, SLUG, intl, usersBase, googleOAuth2Id, userLibApi, jnlpInstaller, userLibInfoNode, user, act }) {

  const { formatMessage } = intl;
  const [fullProjectList, setFullProjectList] = useState(null);
  const [projects, setProjects] = useState(null);
  const [project, setProject] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [listMode, setListMode] = useState(false);

  // Update `fullProjectList`, `projects` and `project`
  useEffect(() => {

    function loadFullProjectList() {
      return fetch(`${usersBase}/${user}/projects.json`)
        .then(checkFetchResponse)
        .then(_fullList => setFullProjectList(_fullList))
        .catch(err => setError(err?.toString() || 'Error'));
    }

    // Clear previous states
    setError(null);
    // Load the given project
    if (act && user && (project === null || project.path !== act)) {
      setLoading(true);
      setProject(null);
      setProjects(null);
      const fullPath = `${usersBase}/${user}/${act}`;
      // Load a specific project
      fetch(`${fullPath}/project.json`)
        .then(checkFetchResponse)
        .then(_project => {
          _project.path = act;
          _project.fullPath = fullPath;
          setProject(_project);
          setLoading(false);
        })
        .catch(err => {
          setError(err?.toString() || 'Error');
        });
    } else if (user && !act) {
      setLoading(true);
      setProject(null);
      if (fullProjectList) {
        setProjects(fullProjectList);
        setLoading(false);
      }
      else
        loadFullProjectList();
    }
  }, [usersBase, act, user, project, fullProjectList]);

  return (
    (error && <h2>{formatMessage({ id: 'error' }, { error })}</h2>) ||
    (loading && <Loading {...{ intl }} />) ||
    (user && project && <Project {...{ intl, user, project, SLUG, jnlpInstaller, location }} />) ||
    (user && projects && <RepoList {...{ intl, user, repoBase: usersBase, projects, listMode, setListMode, setLoading, setError, SLUG, location }} />) ||
    <UserLib path="/" {...{ intl, SLUG, googleOAuth2Id, usersBase, userLibApi, userLibInfoNode }} />
  );
}

export default UserMain;