import React, { useState } from 'react';
import { MDXRenderer } from 'gatsby-plugin-mdx';
import { makeStyles } from "@material-ui/core/styles";
import { Link } from 'gatsby-plugin-intl';
import filesize from 'filesize';
import { mergeClasses, checkFetchResponse } from '../../utils/misc';
import Button from '@material-ui/core/Button';
import CircularProgress from '@material-ui/core/CircularProgress';
import Typography from '@material-ui/core/Typography';
import Avatar from '@material-ui/core/Avatar';

// See: https://github.com/anthonyjgrove/react-google-login
import { GoogleLogin } from 'react-google-login';

const useStyles = makeStyles(theme => ({
  root: {
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
    marginTop: theme.spacing(1),
  },
}));

function UserLib({ intl, SLUG, googleOAuth2Id, userLibApi, userLibInfoNode, ...props }) {

  const classes = mergeClasses(props, useStyles());
  const { locale, defaultLocale, messages, formatMessage } = intl;
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

  const title = userData ? formatMessage({ id: 'user-repo-title' }, { user: userData.fullUserName || userData.id }) : messages['user-repo'];

  const loginSuccess = (googleUser) => {
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
          setUserData({
            googleUser,
            ...data,
          });
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
    setUserData(null);
    setLoading(false);
    setErr(`ERROR: ${details} (${error})`);
  }

  const logout = () => {
    if (userData)
      userData.googleUser.disconnect();
    setUserData(null);
    setErr(null);
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
                isSignedIn={true}
                cookiePolicy={'single_host_origin'}
                render={renderProps => (
                  <Button variant="contained" onClick={renderProps.onClick} disabled={renderProps.disabled}>{messages['user-repo-login']}</Button>
                )}
              />
            }
            {userData &&
              <Button variant="contained" onClick={logout}>{messages['user-repo-logout']}</Button>
            }
          </div>
          {userData &&
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
                  <td>{`${messages['user-repo-num-projects']}:`}</td>
                  <td>{userData.projects.length}</td>
                </tr>
                <tr>
                  <td>{`${messages['user-repo-quota']}:`}</td>
                  <td>{formatMessage({ id: 'user-repo-quota-exp' }, { current: filesize(userData.currentSize), quota: filesize(userData.quota) })}</td>
                </tr>
              </tbody>
            </table>
          }
        </>
      }
    </div>
  );
}

export default UserLib;