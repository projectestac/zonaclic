import React, { useState, useEffect } from 'react';
import { MDXRenderer } from 'gatsby-plugin-mdx';
import { makeStyles } from "@material-ui/core/styles";
import { Link } from 'gatsby-plugin-intl';
import filesize from 'filesize';
import { mergeClasses, checkFetchResponse, clickOnLink } from '../../utils/misc';
import Button from '@material-ui/core/Button';
import IconButton from '@material-ui/core/IconButton';
import CircularProgress from '@material-ui/core/CircularProgress';
import Typography from '@material-ui/core/Typography';
import Avatar from '@material-ui/core/Avatar';
import Divider from '@material-ui/core/Divider';
import AddIcon from '@material-ui/icons/LibraryAdd';
import DeleteIcon from '@material-ui/icons/Delete';
import DownloadIcon from '@material-ui/icons/CloudDownload';
import LoginIcon from '@material-ui/icons/ExitToApp';
import LogoutIcon from '@material-ui/icons/Eject';
import DeleteDialog from './DeleteDialog';

// See: https://github.com/anthonyjgrove/react-google-login
import { GoogleLogin } from 'react-google-login';
import ProjectCard from '../repo/ProjectCard';

const AUTH_KEY = '__auth';

const useStyles = makeStyles(theme => ({
  root: {
  },
  divider: {
    marginTop: theme.spacing(3),
    marginBottom: theme.spacing(3),
  },
  error: {
    color: 'red',
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

function UserLib({ intl, SLUG, googleOAuth2Id, usersBase, userLibApi, userLibInfoNode, ...props }) {

  const classes = mergeClasses(props, useStyles());
  const { messages, formatMessage } = intl;
  const { frontmatter, body } = userLibInfoNode;

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
          // Normalize data fields
          data.projects.forEach(prj => {
            prj.path = prj.basePath;
            if (!prj.totalSize)
              prj.totalSize = prj.totalFileSize;
          })
          const result = { googleUser, ...data };
          sessionStorage.setItem(AUTH_KEY, JSON.stringify(result));
          setUserData(result);
          setErr(null);
        })
        .catch(error => {
          googleUser?.disconnect();
          setErr(error?.toString() || messages['generic-error']);
        })
        .finally(() => {
          setLoading(false);
        });
    }
    else
      setErr(messages['user-repo-login-error']);
  }

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
    console.log('Show the "publish project" dialog...');
  }

  const deleteProject = (project) => (ev) => {
    ev.preventDefault();
    setDeletePrj(project);
  }

  const deleteAction = (project) => {
    console.log(`Project "${project.title}" should be deleted`);
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
            {userData &&
              <Button variant="contained" startIcon={<LogoutIcon />} onClick={logout}>{messages['user-repo-logout']}</Button>
            }
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
              <Divider className={classes['divider']} />
              <Button variant="contained" startIcon={<AddIcon />} onClick={uploadProject}>{messages['user-repo-upload-project']}</Button>
            </>
          }
        </>
      }
      <DeleteDialog {...{ intl, deletePrj, setDeletePrj, deleteAction }} />
    </div>
  );
}

export default UserLib;