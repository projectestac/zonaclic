import React, { useState } from 'react';
import { saveAs } from 'file-saver';
import JSZip from 'jszip';
import { makeStyles } from "@material-ui/core/styles";
import Dialog from '@material-ui/core/Dialog';
import DialogTitle from '@material-ui/core/DialogTitle';
import DialogContent from '@material-ui/core/DialogContent';
import DialogActions from '@material-ui/core/DialogActions';
import Typography from '@material-ui/core/Typography';
import LinearProgress from '@material-ui/core/LinearProgress';
import { mergeClasses } from '../../utils/misc';
import DownloadIcon from '@material-ui/icons/CloudDownload';
import Button from '@material-ui/core/Button';
import PromisePool from '@supercharge/promise-pool';

const useStyles = makeStyles(theme => ({
  root: {
    [theme.breakpoints.up('sm')]: {
      minWidth: '600px',
    },
  },
  dlgContent: {
    "& > *": {
      marginBottom: theme.spacing(2),
    }
  },
  status: {
    maxWidth: '100%',
    whiteSpace: 'nowrap',
    overflow: 'hidden',
    textOverflow: 'ellipsis',
  },
  error: {
    color: theme.palette.error.dark,
  },
  actions: {
    "& > button": {
      margin: theme.spacing(1),
    }

  },
}));

function ProjectDownload({ dlgOpen, setDlgOpen, intl, project, maxThreads = 20, ...props }) {

  const classes = mergeClasses(props, useStyles());

  const { messages, formatMessage } = intl;
  const { title, fullPath, mainFile, files } = project;
  const numFiles = files.length - 1;
  let currentFiles = 0;
  const [msg, setMsg] = useState('message');
  const [status, setStatus] = useState('status');
  const [err, setErr] = useState('error');
  const [progress, setProgress] = useState(0);
  const [zipFile, setZipFile] = useState(null);
  const [progressZip, setProgressZip] = useState(false);
  // Array of `XMLHttpRequest`, used to abort pending requests
  const _xhrs = [];
  // Get the zip file name from the last part of the project path
  const fileParts = fullPath.split('/');
  const zipFileName = `${fileParts[fileParts.length - 1]}.scorm.zip`;

  const reset = () => {
    setMsg(null);
    setStatus(null);
    setErr(null);
    setProgress(0);
    setProgressZip(false);
    setZipFile(null);
    _xhrs.forEach(xhr => {
      if (xhr.readyState < 4) {
        try {
          xhr.abort();
        } catch (err) {
          // Should not occur
          console.log(`Error cancelling XHR: ${err}`, xhr);
        }
      }
    });
    _xhrs.length = 0;
    currentFiles = 0;
  }

  const closeDlg = () => {
    reset();
    setDlgOpen(false);
  };

  /**
   * Retrieves the content of a remote file as a binary object
   *
   * Adapted from Stuart Knightley's [JSZipUtils](https://github.com/Stuk/jszip-utils)
   *
   * This modified version returns an `XMLHttpRequest` object (or `null` in case of error)
   * that can be useful for cancelling pending transactions at user request
   *
   **/
  const getBinaryContent = (path, callback) => {
    let xhr = null;
    try {
      // Create and prepare an XHR
      xhr = new XMLHttpRequest();
      xhr.open('GET', path, true);
      if ('responseType' in xhr)
        xhr.responseType = 'arraybuffer';
      if (xhr.overrideMimeType)
        xhr.overrideMimeType('text/plain; charset=x-user-defined');

      xhr.onreadystatechange = _ev => {
        if (xhr.readyState === 4) {
          if (xhr.status === 200 || xhr.status === 0) {
            let file = null;
            let err = null;
            try {
              // for xhr.responseText, the 0xFF mask is applied by JSZip
              file = xhr.response || xhr.responseText;
            } catch (e) {
              err = new Error(e);
            }
            callback(err, file);
          } else {
            callback(new Error(`downloading "${path}": ${xhr.status} - ${xhr.statusText}`), null);
          }
        }
      };

      // Launch the XHR
      xhr.send();
    } catch (e) {
      xhr = null;
      callback(new Error(e), null);
    }

    // return the resulting XHR, or _null_ in case of error
    return xhr;
  }

  const start = () => {

    // Clear the current dialog
    reset();

    // Run only when `project` is not null
    if (project) {
      setMsg(messages['prj-preparing-scorm']);

      // Max concurrent downloading threads
      // Usually stored as string in the site metadata, should be converted to number
      const threads = Number(maxThreads) || 20;

      // This is where the ingredients will be stored
      const zip = new JSZip();

      // Detect if the main `.jclic` file resides into a subdirectory (usually 'jclic.js/' in projects published on the clicZone library)
      // We will remove this subdirectory in the resulting ZIP file, thus simplifying its internal structure
      const lastSepPos = mainFile.lastIndexOf('/');
      let subdir = lastSepPos >= 0 ? mainFile.substring(0, lastSepPos + 1) : '';
      // Escape all the special characters -/\^$*+?.()[]{} in `subdir`. This usually just replaces '/' by '\/' and '.' by '\.'
      subdir = subdir.replace(/[-/\\^$*+?.()|[\]{}]/g, '\\$&');
      // Build a regExp used to remove all references to `subdir` in `project.json`
      const pathToRemove = new RegExp(subdir, 'g');

      // Make a normalized copy of 'project'
      const prj = JSON.parse(JSON.stringify(project).replace(pathToRemove, ''));
      // Delete extra fields added by `RepoMain`
      delete prj.path;
      delete prj.fullPath;

      // Add the modified 'project.json' to the ZIP file
      zip.file('project.json', JSON.stringify(prj, null, ' '), {});

      // Start downloading ingredients
      setMsg(messages['prj-downloading-ingredients']);

      // Function that creates a download promise for the specified file
      const getFilePromise = file => {
        return new Promise((resolve, reject) => {
          // Call the static method `getBinaryContent` (see below), where the promise will be finally fullfilled or rejected
          const xhr = getBinaryContent(`${fullPath}/${file}`, (err, data) => {
            if (err)
              reject(err);
            else {
              setStatus(file);
              // Compress and save the resulting data in `zip`
              zip.file(file.replace(pathToRemove, ''), data, { binary: true });
              setProgress((++currentFiles) * 100 / numFiles);
              resolve(true);
            }
          });
          if (xhr)
            _xhrs.push(xhr);
        });
      };

      // Function that updates the progress of ZIP compressing
      const zipUpdateInterval = ({ currentFile, percent }) => {
        if (currentFile) {
          setStatus(currentFile);
          setProgress(percent);
        }
      };

      // Function to be executed when all files are downloaded
      // Generates a zip BLOB and stores it on `zipFile`
      const generateZip = () => {
        setMsg(messages['prj-compressing']);
        setStatus('');
        setProgress(0);
        setProgressZip(true);
        return zip.generateAsync({ type: 'blob' }, zipUpdateInterval)
          .then(blob => {
            setMsg(messages['prj-zip-available']);
            setZipFile(blob);
          })
          .catch(err => setErr(`${messages['prj-zip-err']} - ${err}`));
      };

      console.log(`Downloading with ${threads} threads`);

      // Wait for all promises to be fulfilled, using the async promise pool
      PromisePool
        .for(files.filter(f => f !== 'project.json'))
        .withConcurrency(threads)
        .process(getFilePromise)
        .then(generateZip)
        .catch(error => {
          setErr(formatMessage({ id: 'error' }, { error }));
          setStatus('');
        })
        // Clear all references to XMLHttpRequests
        .finally(() => { _xhrs.length = 0; });
    }
  }

  // Gives the resulting BLOB simulating the downloading of a ZIP file
  const downloadFile = () => {
    if (zipFile) {
      // `saveAs` provided by [FileSaver.js](https://github.com/eligrey/FileSaver.js/)
      saveAs(zipFile, zipFileName);
      closeDlg();
    }
  }

  return (
    <Dialog classes={{ paper: classes['root'] }} open={dlgOpen} onClose={closeDlg} onEnter={start}>
      <DialogTitle>{formatMessage({ id: 'prj-download-title' }, { title })}</DialogTitle>
      <DialogContent className={classes['dlgContent']}>
        {!err && <Typography>{msg}</Typography>}
        {!err && !zipFile && !progressZip && <LinearProgress value={progress} variant="determinate" />}
        {!err && !zipFile && progressZip && <LinearProgress value={progress} variant="determinate" color="secondary" />}
        {!err && !zipFile && <Typography className={classes['status']}>{status}</Typography>}
        {err && <Typography className={classes['error']}>{err}</Typography>}
      </DialogContent>
      <DialogActions className={classes['actions']}>
        {zipFile &&
          <Button
            variant="contained"
            startIcon={<DownloadIcon />}
            onClick={downloadFile}
          >
            {messages['prj-download-file']}
          </Button>
        }
        <Button
          variant="contained"
          onClick={closeDlg}
        >
          {messages['cancel']}
        </Button>
      </DialogActions>
    </Dialog>
  );
}

export default ProjectDownload;
