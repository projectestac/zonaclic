import React, { useState, useEffect } from 'react';
import { useIntl } from 'gatsby-plugin-intl';
import { makeStyles } from '@material-ui/core/styles';
import Layout from '../components/Layout';
import SEO from '../components/SEO';

const useStyles = makeStyles(theme => ({
  root: {
    ...theme.typography.body1,
    '& h1': { ...theme.typography.h1 },
  },
}));

export default function NotFoundPage() {
  const intl = useIntl();
  const { locale: lang, formatMessage } = intl;
  const classes = useStyles();
  const [path, setPath] = useState('');

  useEffect(() => {
    if (window)
      setPath(window.location.pathname);
  }, []);

  return (
    <Layout {...{ intl }}>
      <SEO title="404: Not Found" {...{ lang }} />
      <article className={classes.root}>
        <h1>{intl.messages['404-not-found']}</h1>
        <p>{formatMessage({ id: 'not-found' }, { path })}</p>
      </article>
    </Layout>
  );
}
