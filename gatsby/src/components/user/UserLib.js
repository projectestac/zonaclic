import React, { useState, useEffect } from 'react';
import { graphql, useStaticQuery } from 'gatsby';
import { MDXRenderer } from 'gatsby-plugin-mdx';
import { makeStyles } from "@material-ui/core/styles";
import { Link } from 'gatsby-plugin-intl';
import filesize from 'filesize';
import { mergeClasses, checkFetchResponse, clickOnLink } from '../../utils/misc';
import { getResolvedVersionForLanguage } from '../../utils/node';
import Button from '@material-ui/core/Button';
import IconButton from '@material-ui/core/IconButton';
import CircularProgress from '@material-ui/core/CircularProgress';
import Typography from '@material-ui/core/Typography';
import Avatar from '@material-ui/core/Avatar';
import AddIcon from '@material-ui/icons/LibraryAdd';
import DeleteIcon from '@material-ui/icons/Delete';
import DownloadIcon from '@material-ui/icons/CloudDownload';
import LoginIcon from '@material-ui/icons/ExitToApp';
import LogoutIcon from '@material-ui/icons/Eject';
import InfoIcon from '@material-ui/icons/Info';
import DeleteDialog from './DeleteDialog';
import UploadDialog from './UploadDialog';
// See: https://github.com/anthonyjgrove/react-google-login
import { GoogleLogin } from 'react-google-login';
import ProjectCard from '../repo/ProjectCard';

const AUTH_KEY = '__auth';

const query = graphql`
  query {
    allMdx(filter: {fields: {slug: {eq: "/userlib/"}}}) {
      edges {
        node {
          id
          body
          fields {
            lang
            slug
          }
          frontmatter {
            title
          }
        }
      }
    }    
  }
`;

const useStyles = makeStyles(theme => ({
  root: {
  },
  error: {
    color: theme.palette.error.dark,
    fontWeight: 'bold',
  },
  titleGroup: {
    display: 'grid',
    gridTemplateColumns: '1fr auto',
    gridGap: theme.spacing(1),
  },
  avatar: {
    width: theme.spacing(7),
    height: theme.spacing(7),
  },
  mainButtons: {
    marginTop: theme.spacing(3),
    '& > *': {
      marginRight: theme.spacing(2),
      marginBottom: theme.spacing(2),
    },
  },
  projects: {
    marginTop: theme.spacing(3),
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(14rem, 1fr))',
    gridGap: '1rem',
    "& a:link": {
      textDecoration: 'none',
    }
  },
  cardInfo: {
    display: 'flex',
    alignItems: 'center',
    "& *:first-child": {
      flexGrow: 1,
    },
  }
}));

function UserLib({ intl, SLUG, googleOAuth2Id, usersBase, userLibApi, ...props }) {

  const classes = mergeClasses(props, useStyles());
  const { frontmatter, body } = getResolvedVersionForLanguage(useStaticQuery(query), intl);
  const { messages, formatMessage } = intl;

  /**
   * userData fields: {
   *   googleUser,
   *   status (vaildated|error),
   *   id, fullUserName, email, avatar,
   *   expires (ISO date),
   *   currentSize (bytes), quota (bytes),
   *   projects: [
   *     { basePath, name, title, cover, thumbnail, mainFile,
   *       author, school, date,
   *       meta_langs, description: {lang:desc,}, langCodes: [lang,], languages: {lang:langName,}, levels: {lang:level,}, areas: {lang:area,},
   *       files: [file,], totalFileSize(bytes)
   *     },
   *   ]
   * }
   */
  const [userData, setUserData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState(null);
  const [deletePrj, setDeletePrj] = useState(null);
  const [uploadDlg, setUploadDlg] = useState(false);

  const title = userData ? formatMessage({ id: 'user-repo-title' }, { user: userData.fullUserName || userData.id }) : messages['user-repo'];

  useEffect(() => {
    if (!userData) {
      const obj = JSON.parse(sessionStorage.getItem(AUTH_KEY));
      if (obj && obj.googleUser && obj.expires && Date.now() < new Date(obj.expires))
        loginSuccess(obj.googleUser);
    }
  });

  const loginSuccess = (googleUser) => {
    sessionStorage.removeItem(AUTH_KEY);
    if (googleUser && googleUser.tokenId) {
      setLoading(true);
      fetch(`${userLibApi}/getUserInfo`, {
        method: 'POST',
        mode: 'cors',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
          'Accept': 'application/json',
        },
        body: `id_token=${googleUser.tokenId}`,
      })
        .then(checkFetchResponse)
        .then(data => {
          if (!data || data.status !== 'validated') {
            throw new Error(data?.error);
          }
          data.projects.forEach(normalizeProjectFields);
          const result = { googleUser, ...data };
          sessionStorage.setItem(AUTH_KEY, JSON.stringify(result));
          setUserData(result);
          setErr(null);
        })
        .catch(error => {
          if (googleUser.disconnect)
            googleUser.disconnect();
          setErr(error?.toString() || messages['generic-error']);
        })
        .finally(() => {
          setLoading(false);
        });
    }
    else
      setErr(messages['user-repo-login-error']);
  }

  const normalizeProjectFields = project => {
    if (project.basePath)
      project.path = project.basePath;
    if (!project.totalSize)
      project.totalSize = project.totalFileSize;
    return project;
  };

  const loginFailed = ({ error, details }) => {
    sessionStorage.removeItem(AUTH_KEY);
    setUserData(null);
    setLoading(false);
    setErr(`ERROR: ${details} (${error})`);
  }

  const logout = () => {
    if (userData && typeof userData?.googleUser?.disconnect === 'function')
      userData.googleUser.disconnect();
    sessionStorage.removeItem(AUTH_KEY);
    setUserData(null);
    setErr(null);
  }

  const uploadProject = () => {
    setUploadDlg(true);
  }

  const deleteProject = (project) => (ev) => {
    ev.preventDefault();
    setDeletePrj(project);
  }

  const deleteAction = (project) => {
    setDeletePrj('waiting');
    console.log(`Project "${project.title}" should be deleted`);

    fetch(`${userLibApi}/deleteProject`, {
      method: 'POST',
      mode: 'cors',
      cache: 'no-cache',
      credentials: 'same-origin',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
        'Accept': 'application/json',
      },
      body: `project=${project.name}`,
    })
      .then(checkFetchResponse)
      .then(response => {
        if (response.status === 'ok' ||
          // Workaround for NFS delay when deleting files
          (response.status === 'error' && response.error && response.error.indexOf('Unable to delete directory') >= 0)) {
          const updatedUserData = { ...userData };
          updatedUserData.projects = userData.projects.filter(prj => prj.name !== project.name);
          updatedUserData.currentSize -= project.totalSize;
          setUserData(updatedUserData);
        }
        else
          throw new Error(response.error || messages['unknown-error']);
      })
      .catch(error => {
        alert(formatMessage({ id: 'user-repo-delete-err' }, { error: error?.toString() || messages['generic-error'] }));
      })
      .finally(() => {
        setDeletePrj(null);
      });
  }

  const uploadAction = (file, folder) => {
    const data = new FormData();
    data.append('scormFile', file);
    data.append('project', folder);
    fetch(`${userLibApi}/uploadUserFile`, {
      method: 'POST',
      mode: 'cors',
      cache: 'no-cache',
      credentials: 'same-origin',
      body: data,
    })
      .then(checkFetchResponse)
      .then(response => {
        if (response.status === 'ok' && response.project) {
          const project = normalizeProjectFields(response.project);
          const updatedUserData = { ...userData };
          const updatedProject = userData.projects.find(prj => prj.name === project.name);
          if (updatedProject) {
            updatedUserData.currentSize -= updatedProject.totalSize;
            updatedUserData.projects = userData.projects.filter(prj => prj.name !== project.name);
          }
          updatedUserData.projects.push(project);
          updatedUserData.currentSize += project.totalSize;
          setUserData(updatedUserData);
        }
        else
          throw new Error(response.error || messages['unknown-error']);
      })
      .catch(error => {
        alert(formatMessage({ id: 'user-repo-delete-err' }, { error: error?.toString() || messages['generic-error'] }));
      })
      .finally(() => {
        setUploadDlg(false);
      });
  }

  const downloadProject = (project) => (ev) => {
    ev.preventDefault();
    clickOnLink(`${userLibApi}/downloadUserProject?prj=${userData.id}/${project.basePath}`);
  }

  return (
    <div className={classes.root}>
      <div className={classes['titleGroup']}>
        <Typography variant="h2">{title}</Typography>
        {userData && <Avatar alt={userData.fullUserName} src={userData.avatar} className={classes['avatar']} />}
      </div>
      <MDXRenderer {...{ frontmatter, intl }}>{body}</MDXRenderer>
      {err && <div className={classes['error']}>{err}</div>}
      {loading && <CircularProgress className={classes['loading']} />}
      {!loading &&
        <>
          <div className={classes['mainButtons']}>
            {!userData &&
              <GoogleLogin
                clientId={googleOAuth2Id}
                buttonText={messages['user-repo-login']}
                onSuccess={loginSuccess}
                onFailure={loginFailed}
                isSignedIn={false}
                cookiePolicy={'single_host_origin'}
                render={renderProps => (
                  <Button variant="contained" startIcon={<LoginIcon />}
                    onClick={renderProps.onClick} disabled={renderProps.disabled}>{messages['user-repo-login']}</Button>
                )}
              />
            }
            {userData && <Button variant="contained" startIcon={<LogoutIcon />} onClick={logout}>{messages['user-repo-logout']}</Button>}
            {userData && <Button variant="contained" startIcon={<AddIcon />} onClick={uploadProject}>{messages['user-repo-upload-project']}</Button>}
          </div>
          {userData &&
            <>
              <table className="dataCard">
                <tbody>
                  <tr>
                    <td>{`${messages['user-repo-user']}:`}</td>
                    <td>{userData.fullUserName || userData.id} ({userData.email})</td>
                  </tr>
                  <tr>
                    <td>{`${messages['user-repo-library']}:`}</td>
                    <td><Link to={`${SLUG}?user=${userData.id}`}>{`${window.location.href}?user=${userData.id}`}</Link></td>
                  </tr>
                  <tr>
                    <td>{`${messages['user-repo-projects']}:`}</td>
                    <td>{userData.projects.length}</td>
                  </tr>
                  <tr>
                    <td>{`${messages['user-repo-quota']}:`}</td>
                    <td>{formatMessage({ id: 'user-repo-quota-exp' }, { current: filesize(userData.currentSize), quota: filesize(userData.quota) })}</td>
                  </tr>
                </tbody>
              </table>
              <Typography variant="h3" color="primary">{messages['user-repo-projects']}</Typography>
              {(userData.projects.length === 0 && <p>{messages['user-repo-no-projects']}</p>) ||
                <div className={classes['projects']}>
                  {userData.projects.map((project, n) => (
                    <ProjectCard {...{ key: n, SLUG, user: userData.id, messages, repoBase: usersBase, project }} >
                      <div className={classes['cardInfo']}>
                        <div>
                          {`${messages['prj-size']}: ${filesize(project.totalSize)}`}<br />
                          {`${messages['prj-numfiles']}: ${project.files.length}`}
                        </div>
                        <IconButton
                          aria-label={messages['prj-more-info']}
                          title={messages['prj-more-info']}
                          color="primary"
                        >
                          <InfoIcon />
                        </IconButton>
                        <IconButton
                          onClick={downloadProject(project)}
                          aria-label={messages['prj-download']}
                          title={messages['prj-download']}
                          color="primary"
                        >
                          <DownloadIcon />
                        </IconButton>
                        <IconButton
                          onClick={deleteProject(project)}
                          aria-label={messages['user-repo-delete-project']}
                          title={messages['user-repo-delete-project']}
                          color="primary"
                        >
                          <DeleteIcon />
                        </IconButton>
                      </div>
                    </ProjectCard>
                  ))}
                </div>
              }
            </>
          }
        </>
      }
      <DeleteDialog {...{ intl, deletePrj, setDeletePrj, deleteAction }} />
      <UploadDialog {...{ intl, uploadDlg, setUploadDlg, userData, uploadAction }} />
    </div>
  );
}

export default UserLib;