import React, { useState } from 'react';
import { makeStyles } from "@material-ui/core/styles";
import { mergeClasses } from '../../utils/misc';
import Button from '@material-ui/core/Button';

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
  }
}));

function UserLib({ intl, googleOAuth2Id, ...props }) {

  const classes = mergeClasses(props, useStyles());
  const { locale, defaultLocale, messages, formatMessage } = intl;

  const [userData, setUserData] = useState(null);
  const [err, setErr] = useState(null);

  const loginSuccess = (data) => {
    if (data) {
      setUserData({
        googleUser: data, // tokenId ...
        ...data?.profileObj, // googleId, imageUrl, email, name, givenName, familyName
      });
      setErr(null);
    }
  }

  const loginFailed = ({ error, details }) => {
    setUserData(null);
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
      {err && <div className={classes['error']}>{err}</div>}
      {!userData &&
        <GoogleLogin
          clientId={googleOAuth2Id}
          buttonText={messages['user-repo-login']}
          onSuccess={loginSuccess}
          onFailure={loginFailed}
          isSignedIn={true}
          cookiePolicy={'single_host_origin'}
        />
      }
      {userData &&
        <div className={classes['userInfo']}>
          <img src={userData.imageUrl} alt="avatar" />
          <p>{userData.name}</p>
          <Button variant="contained" onClick={logout}>{messages['user-repo-logout']}</Button>
        </div>
      }
    </div>
  );
}

export default UserLib;