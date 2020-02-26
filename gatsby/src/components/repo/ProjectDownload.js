import React from 'react';
import { withPrefix } from 'gatsby';
import { saveAs } from 'file-saver';
import JSZip from 'jszip';
import { makeStyles } from "@material-ui/core/styles";
import { mergeClasses, htmlContent } from '../../utils/misc';
import DownloadIcon from '@material-ui/icons/CloudDownload';
import Button from '@material-ui/core/Button';

const useStyles = makeStyles(_theme => ({
  root: {
  },
}));


function ProjectDownload({ intl, project, SLUG, REPO_BASE, location, fullProjectList, ...props }) {

  const classes = mergeClasses(props, useStyles());

  return (
    <div>
      Hola!
    </div>
  );
}

export default ProjectDownload;
