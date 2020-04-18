import React, { useState } from 'react';
import { graphql, useStaticQuery } from 'gatsby';
import { MDXRenderer } from 'gatsby-plugin-mdx';
import { makeStyles } from "@material-ui/core/styles";
import { mergeClasses } from '../../utils/misc';
import { getResolvedVersionForLanguage } from '../../utils/node';
import filesize from 'filesize';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import TextField from '@material-ui/core/TextField';
import LinearProgress from '@material-ui/core/LinearProgress';

const query = graphql`
  query {
    allMdx(filter: {fields: {slug: {eq: "/userlib/upload/"}}}) {
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
  content: {
    '& > *': {
      marginTop: theme.spacing(2),
      marginBottom: theme.spacing(2),
    },
  },
  input: {
    display: 'none',
  },
  info: {
    height: '4rem',
  },
  fileInfo: {
    marginTop: theme.spacing(2),
  },
  fileName: {
    fontWeight: 'bold',
  },
  folder: {
    width: '100%',
    maxWidth: '18rem',
  },
  error: {
    color: theme.palette.error.dark,
  },
  warning: {
    color: theme.palette.warning.dark,
  },
  waiting: {
    marginTop: theme.spacing(2),
    '& > *': {
      marginTop: theme.spacing(1),
      marginBottom: theme.spacing(1),
    },
  },
}));

function UploadDialog({ intl, uploadDlg, setUploadDlg, userData, uploadAction, ...props }) {

  const classes = mergeClasses(props, useStyles());
  const { frontmatter, body } = getResolvedVersionForLanguage(useStaticQuery(query), intl);

  const { messages } = intl;
  const [file, setFile] = useState(null);
  const [folder, setFolder] = useState('');
  const [err, setErr] = useState(null);
  const [warn, setWarn] = useState(null);
  const [ready, setReady] = useState(false);
  const [waiting, setWaiting] = useState(false);
  const availSpace = userData ? userData.quota - userData.currentSize : 0;

  const reset = () => {
    setFile(null);
    setFolder('');
    setErr(null);
    setWarn(null);
    setReady(false);
    setWaiting(false);
  }

  const handleUploadClick = ev => {
    if (ev.target.files && ev.target.files.length > 0) {
      const file = ev.target.files[0];
      const folder = file.name.substring(0, file.name.indexOf('.')).trim().replace(/[\W]/gi, '_').toLowerCase() || 'prj';
      setFile(file);
      setFolder(folder);
      checkErrors(file, folder);
    }
  }

  const updateFolder = ev => {
    const folder = ev.target.value;
    setFolder(folder);
    checkErrors(file, folder);
  }

  const checkErrors = (file, folder) => {
    let err = null;
    if (!file.name.toLowerCase().endsWith('.scorm.zip'))
      err = messages['user-repo-err-bad-type'];
    else if (file.size > availSpace)
      err = messages['user-repo-err-quota'];
    else if (!folder)
      err = messages['user-repo-err-no-name'];
    else if (/[\W]/gi.test(folder))
      err = messages['user-repo-err-bad-chars'];

    setErr(err);
    setReady(err === null);

    if (folder && userData.projects.find(prj => prj.name === folder))
      setWarn(messages['user-repo-warn-dir-exists']);
    else
      setWarn(null);
  }

  const uploadProject = () => {
    if (file && folder && !err) {
      setWaiting(true);
      setReady(false);
      uploadAction(file, folder);
    }
  }

  return (
    <Dialog
      className={classes['root']}
      open={uploadDlg}
      onExit={reset}
      aria-labelledby="upload-dialog-title"
    >
      <DialogTitle id="upload-dialog-title">{messages['user-repo-upload-title']}</DialogTitle>
      <DialogContent className={classes['content']}>
        <MDXRenderer {...{ frontmatter, intl }}>{body}</MDXRenderer>
        <input
          type="file"
          accept=".scorm.zip"
          className={classes['input']}
          id="input-file"
          onChange={handleUploadClick}
          disabled={waiting}
        />
        <label htmlFor="input-file">
          <Button component="span" variant="contained" disabled={waiting}>{messages['user-repo-upload-select-file']}</Button>
        </label>
        {file &&
          <>
            <div className={classes['fileInfo']}>
              {messages['user-repo-upload-selected-file']} <span className={classes['fileName']}>{file.name}</span> ({filesize(file.size)})
            </div>
            <TextField
              className={classes['folder']}
              label={messages['user-repo-upload-current-directory']}
              value={folder}
              onChange={updateFolder}
              disabled={waiting}
            />
          </>
        }
        <div className={classes['info']}>
          {warn && <div className={classes['warning']}>{warn}</div>}
          {err && <div className={classes['error']}>{err}</div>}
          {waiting &&
            <div className={classes['waiting']}>
              <div>{messages['user-repo-uploading']}</div>
              <LinearProgress />
            </div>
          }
        </div>
      </DialogContent>
      <DialogActions>
        <Button onClick={uploadProject} disabled={!ready || waiting} color="primary">{messages['user-repo-upload-publish']}</Button>
        <Button onClick={() => setUploadDlg(false)} color="primary" disabled={waiting}>{messages['cancel']}</Button>
      </DialogActions>
    </Dialog>
  );
}

export default UploadDialog;