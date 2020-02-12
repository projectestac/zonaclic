import React from 'react';
import { navigate } from 'gatsby-plugin-intl';
import { makeStyles } from "@material-ui/core/styles";
import { mergeClasses } from '../../utils/misc';
import Container from "@material-ui/core/Container";
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemAvatar from '@material-ui/core/ListItemAvatar';
import Avatar from '@material-ui/core/Avatar';
import ListItemText from '@material-ui/core/ListItemText';
import TablePagination from '@material-ui/core/TablePagination';
import { FontAwIcon } from '../../utils/FontAwIcon';
import SEO from '../SEO';
import { getAllVariants } from '../../utils/node';

const DEFAULT_ITEMS_PER_PAGE = 10;

const useStyles = makeStyles(theme => ({
  root: {
  },
}));

function RepoList({ intl: { locale, messages, formatMessage }, projects, SLUG, REPO_BASE, location, ...props }) {

  const classes = mergeClasses(props, useStyles());
  const [page, setPage] = React.useState(0);
  const [itemsPerPage, setItemsPerPage] = React.useState(DEFAULT_ITEMS_PER_PAGE);
  const alt = getAllVariants(SLUG, location, locale);
  const title = messages['repo-title'];
  const description = messages['repo-description'];

  return (
    <>
      <SEO {...{ locale, title, description, slug: SLUG, thumbnail: `/cards/repo/twitter-card-${locale}.jpg`, alt }} />
      <Container {...props} className={classes.root}>
        <List>
          {projects
            .slice(page * itemsPerPage, (page + 1) * itemsPerPage)
            .map(({ path, title, author, date, langCodes, levelCodes, mainFile, cover, thumbnail }, n) => (
              <ListItem button key={n} onClick={() => navigate(`/repo/?act=${path}`, { replace: false })}>
                <ListItemAvatar>
                  <Avatar alt={title} src={`${REPO_BASE}${path}/${thumbnail || cover}`} />
                </ListItemAvatar>
                <ListItemText primary={title} secondary={author} />
              </ListItem>
            ))}
        </List>
        <TablePagination
          classes={{ spacer: classes.spacer, toolbar: classes.toolbar }}
          component="nav"
          page={page}
          rowsPerPage={itemsPerPage}
          onChangeRowsPerPage={ev => setItemsPerPage(ev.target.value)}
          count={projects.length}
          onChangePage={(_ev, p) => setPage(p)}
          labelDisplayedRows={({ from, to, count }) => formatMessage({ id: 'search-results-count' }, { from, to, count })}
          labelRowsPerPage={messages['search-results-per-page']}
        />
      </Container>
    </>
  );
}

export default RepoList;