import React, { useState } from 'react';
import { MDXRenderer } from 'gatsby-plugin-mdx';
import { makeStyles } from "@material-ui/core/styles";
import { mergeClasses, checkFetchResponse } from '../../utils/misc';
import Button from '@material-ui/core/Button';
import CircularProgress from '@material-ui/core/CircularProgress';

// See: https://github.com/anthonyjgrove/react-google-login
import { GoogleLogin } from 'react-google-login';

const useStyles = makeStyles(theme => ({
  root: {
  },
  error: {
    color: 'red',
  },
  userInfo: {
    color: 'blue',
  },
}));

function UserLib({ intl, googleOAuth2Id, userLibApi, userLibInfoNode, ...props }) {

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
      <p>{messages['user-repo']}</p>
      <MDXRenderer {...{ frontmatter, intl }}>{body}</MDXRenderer>
      {err && <div className={classes['error']}>{err}</div>}
      {loading && <CircularProgress className={classes['loading']} />}
      {!userData && !loading &&
        <GoogleLogin
          clientId={googleOAuth2Id}
          buttonText={messages['user-repo-login']}
          onSuccess={loginSuccess}
          onFailure={loginFailed}
          isSignedIn={true}
          cookiePolicy={'single_host_origin'}
        />
      }
      {userData && !loading &&
        <div className={classes['userInfo']}>
          <img src={userData.avatar} alt="avatar" />
          <p>{userData.fullUserName}</p>
          <Button variant="contained" onClick={logout}>{messages['user-repo-logout']}</Button>
        </div>
      }
    </div>
  );
}

export default UserLib;