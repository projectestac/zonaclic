import React, { useState } from 'react';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import LinearProgress from '@material-ui/core/LinearProgress';

function DeleteDialog({ intl, deletePrj, setDeletePrj, deleteAction }) {

  const { messages, formatMessage } = intl;

  return (
    <Dialog
      open={deletePrj !== null}
      onClose={() => setDeletePrj(null)}
      aria-labelledby="alert-dialog-title"
      aria-describedby="alert-dialog-description"
    >
      <DialogTitle id="alert-dialog-title">{messages['user-repo-delete']}</DialogTitle>
      <DialogContent>
        <DialogContentText id="alert-dialog-description">
          {deletePrj === 'waiting' ?
            messages['user-repo-delete-wait'] :
            formatMessage({ id: 'user-repo-confirm-delete' }, { project: deletePrj?.title })
          }
        </DialogContentText>
        {deletePrj === 'waiting' && <LinearProgress style={{marginTop: '2rem', marginBottom: '3rem'}}/>}
      </DialogContent>
      {deletePrj !== 'waiting' &&
        <DialogActions>
          <Button onClick={() => setDeletePrj(null)} color="primary">{messages['cancel']}</Button>
          <Button onClick={() => deleteAction(deletePrj)} color="primary" autoFocus>{messages['user-repo-delete-project']}</Button>
        </DialogActions>
      }
    </Dialog>
  );
}

export default DeleteDialog;