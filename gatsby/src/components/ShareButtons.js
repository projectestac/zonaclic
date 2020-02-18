import React, { useState } from 'react';
import { useStaticQuery, graphql } from 'gatsby';
import { getImgUrl } from '../utils/misc';
import IconButton from '@material-ui/core/IconButton';
import { makeStyles } from '@material-ui/core/styles';
import { mergeClasses } from '../utils/misc';
import Paper from '@material-ui/core/Paper';
import Input from '@material-ui/core/Input';
import InputAdornment from '@material-ui/core/InputAdornment';
import Snackbar from '@material-ui/core/Snackbar';
import RadioGroup from '@material-ui/core/RadioGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Radio from '@material-ui/core/Radio';
import { CopyToClipboard } from 'react-copy-to-clipboard';
import SvgIcon from '@material-ui/core/SvgIcon';
import EmailIcon from '@material-ui/icons/Email';
import FacebookIcon from '@material-ui/icons/Facebook';
import TwitterIcon from '@material-ui/icons/Twitter';
import TelegramIcon from '@material-ui/icons/Telegram';
import PinterestIcon from '@material-ui/icons/Pinterest';
import WhatsAppIcon from '@material-ui/icons/WhatsApp';
import CodeIcon from '@material-ui/icons/Code';
import CopyIcon from '@material-ui/icons/FileCopyOutlined';
import CloseIcon from '@material-ui/icons/Close';

const query = graphql`
  query {
    site {
      siteMetadata {
        baseUrl
        pathPrefix
        cardFileName
        shareSites {
          twitter
          facebook
          telegram
          whatsapp
          pinterest
          email
          classroom
          moodle
          embed
        }
        shareMeta {
          hash
          via
        }
        facebookId
      }
    }
  }
`;

const useStyles = makeStyles(_theme => ({
  root: {
    position: 'relative',
  },
  buttons: {
    display: 'flex',
    flexWrap: 'wrap',
    "& button": {
      marginLeft: '-0.7rem',
    }
  },
  moodleBox: {
    padding: '1rem',
    marginBottom: '1rem',
  },
  embedBox: {
    padding: '1rem',
    marginBottom: '1rem',
  },
  radioGroup: {
    flexDirection: 'row',
    justifyContent: 'center',
  },
  twitter: {
    color: '#01acee',
  },
  facebook: {
    color: '#3c5a98',
  },
  telegram: {
    color: '#37aee2',
  },
  whatsapp: {
    color: '#2cb842',
  },
  pinterest: {
    color: '#cb2128',
  },
  email: {
    color: '#0a5191',
  },
}));

// Google Classroom icon
export const GClassRoomIcon = () =>
  <SvgIcon viewBox="0 0 48 48">
    <path d="M41 42H7c-2.207 0-4-1.793-4-4V10c0-2.207 1.793-4 4-4h34c2.207 0 4 1.793 4 4v28c0 2.207-1.793 4-4 4z" fill="#FFC107" />
    <path d="M7 10h34v28H7z" fill="#388E3C" /><path d="M28 36h8v2h-8zM27 20a3 3 0 11-6.002-.002A3 3 0 0127 20z" fill="#FFF" />
    <path d="M18 23a1.999 1.999 0 11-4 0 1.999 1.999 0 114 0z" fill="#A5D6A7" />
    <path d="M7 10h34v2H7z" fill="#2E7D32" /><path d="M36 38h-8l4 4h8z" fill="#FFAB00" />
    <path d="M34 23a1.999 1.999 0 11-4 0 1.999 1.999 0 114 0zM37 28.688c0-.446-.164-.875-.469-1.2C35.84 26.75 34.363 26 32 26c-2.363 0-3.84.75-4.531 1.488a1.747 1.747 0 00-.469 1.2V30h10zM21 28.688c0-.446-.164-.875-.469-1.2C19.84 26.75 18.363 26 16 26c-2.363 0-3.84.75-4.531 1.488a1.747 1.747 0 00-.469 1.2V30h10z" fill="#A5D6A7" />
    <path d="M30 27.742c0-.535-.195-1.047-.563-1.437C28.605 25.418 26.837 24 24 24c-2.836 0-4.605 1.418-5.438 2.305A2.08 2.08 0 0018 27.742V30h12z" fill="#FFF" />
  </SvgIcon>;

// Moodle icon
export const MoodleIcon = () =>
  <SvgIcon viewBox="0 0 48 48">
    <path fill="#ffab40" d="M33.5 16c-2.5 0-4.8 1-6.5 2.6-1.7-1.6-4-2.6-6.5-2.6-5.2 0-9.5 4.3-9.5 9.5V37h6V24.5c0-1.9 1.6-3.5 3.5-3.5s3.5 1.6 3.5 3.5V37h6V24.5c0-1.9 1.6-3.5 3.5-3.5s3.5 1.6 3.5 3.5V37h6V25.5c0-5.2-4.3-9.5-9.5-9.5z" />
    <path d="M5.5 16.2h1V32h-1z" />
    <path fill="#424242" d="M22 13c1.1.4 2.6 2 3 3-1.8 1.7-2.6 2.9-3 6-.1 1.1-.9 1.7-2 1-3.1-1.9-6-2-8-2-1-1-.5-3.7 0-5l6 1 4-4z" />
    <path fill="#616161" d="M18 17H4l11-7h14l-11 7z" />
    <path fill="#424242" d="M7.5 30c0-2.2-.7-4-1.5-4s-1.5 1.8-1.5 4 .7 4 1.5 4 1.5-1.8 1.5-4z" />
  </SvgIcon>;

const E = encodeURIComponent;



export default function ShareButtons({ shareSites = {}, shareMeta = {}, intl, slug, thumbnail, link, moodleLink, embedOptions, title, description, emailBody, ...props }) {

  const classes = mergeClasses(props, useStyles());
  const [embedBox, setEmbedBox] = useState(false);
  const [moodleBox, setMoodleBox] = useState(false);
  const [snackOpen, setSnackOpen] = useState(false);
  const handleSnackClose = (_ev, reason) => { if (reason !== 'clickaway') setSnackOpen(false); };
  const buildEmbedCode = (options) => options ? `<iframe ${Object.keys(options).map(key => `${key}="${options[key]}"`).join(' ')}></iframe>` : null;
  const [embedCode, setEmbedCode] = useState(buildEmbedCode(embedOptions));
  const [embedSize, setEmbedSize] = useState('800x600');
  const handleChangeEmbedSize = (ev) => {
    setEmbedSize(ev.target.value);
    const wh = ev.target.value.split('x');
    setEmbedCode(buildEmbedCode({ ...embedOptions, width: wh[0], height: wh[1] }));
  }
  const { site: { siteMetadata } } = useStaticQuery(query);
  const { twitter, facebook, telegram, whatsapp, pinterest, email, classroom, moodle, embed } = { ...siteMetadata.shareSites, ...shareSites };
  const { hash, via } = { ...siteMetadata.shareMeta, shareMeta };
  const { messages, locale: lang } = intl;
  const img = getImgUrl({ siteMetadata, slug, lang, thumbnail });

  return (
    <div className={classes.root}>
      <div className={classes.buttons}>
        {twitter && title && link &&
          <a
            href={`https://twitter.com/intent/tweet?text=${E(title)}&url=${E(link)}${hash ? `&hashtags=${E(hash)}` : ''}${via ? `&via=${E(via)}` : ''}`}
            target="_blank"
            rel="noopener noreferrer">
            <IconButton className={classes.twitter} aria-label={messages['share-twitter']} title={messages['share-twitter']}>
              <TwitterIcon />
            </IconButton>
          </a>
        }
        {facebook && title && link &&
          <a
            href={`https://www.facebook.com/dialog/feed?app_id=${siteMetadata.facebookId}&link=${E(link)}${img ? `&picture=${E(img)}` : ''}&name=${E(title)}${description ? `&description=${E(description)}` : ''}&redirect_uri=${E('https://facebook.com')}`}
            target="_blank"
            rel="noopener noreferrer">
            <IconButton className={classes.facebook} aria-label={messages['share-facebook']} title={messages['share-facebook']}>
              <FacebookIcon />
            </IconButton>
          </a>
        }
        {telegram && title && link &&
          <a
            href={`https://telegram.me/share/url?url=${E(link)}&text=${E(`${title}\n${description || ''}`)}`}
            target="_blank"
            rel="noopener noreferrer">
            <IconButton className={classes.telegram} aria-label={messages['share-telegram']} title={messages['share-telegram']}>
              <TelegramIcon />
            </IconButton>
          </a>}
        {whatsapp && title && link &&
          <a
            href={`https://api.whatsapp.com/send?text=${E(`${title}\n${link}`)}`}
            target="_blank"
            rel="noopener noreferrer">
            <IconButton className={classes.whatsapp} aria-label={messages['share-whatsapp']} title={messages['share-whatsapp']}>
              <WhatsAppIcon />
            </IconButton>
          </a>
        }
        {pinterest && img && title && link &&
          <a
            href={`https://pinterest.com/pin/create/button/?url=${E(link)}&media=${E(img)}&description=${E(title)}`}
            target="_blank"
            rel="noopener noreferrer">
            <IconButton className={classes.pinterest} aria-label={messages['share-pinterest']} title={messages['share-pinterest']}>
              <PinterestIcon />
            </IconButton>
          </a>
        }
        {email && title &&
          <a
            href={`mailto:?subject=${E(title)}&body=${E(emailBody || `${title}\n\n${description || ''}\n${link}`)}`}
            target="_blank"
            rel="noopener noreferrer">
            <IconButton className={classes.email} aria-label={messages['share-email']} title={messages['share-email']} >
              <EmailIcon />
            </IconButton>
          </a>
        }
        {classroom && link &&
          <a
            href={`https://classroom.google.com/u/0/share?url=${E(link)}`}
            target="_blank"
            rel="noopener noreferrer">
            <IconButton aria-label={messages['share-classroom']} title={messages['share-classroom']} >
              <GClassRoomIcon />
            </IconButton>
          </a>
        }
        {embed && embedCode &&
          <IconButton
            aria-label={messages['share-embed']}
            title={messages['share-embed']}
            onClick={() => {
              setSnackOpen(false);
              setMoodleBox(false);
              setEmbedBox(!embedBox);
            }}>
            <CodeIcon />
          </IconButton>
        }
        {moodle && moodleLink &&
          <IconButton
            aria-label={messages['share-moodle']}
            title={messages['share-moodle']}
            onClick={() => {
              setSnackOpen(false);
              setMoodleBox(!moodleBox);
              setEmbedBox(false);
            }}>
            <MoodleIcon />
          </IconButton>
        }
      </div>
      {moodle && moodleLink && moodleBox &&
        <Paper className={classes['moodleBox']} elevation={2}>
          <label htmlFor="moodleLink" dangerouslySetInnerHTML={{ __html: messages['share-moodle-label'] }} />
          <Input
            type="text"
            fullWidth
            id="moodleLink"
            value={moodleLink}
            inputProps={{
              readOnly: true,
            }}
            endAdornment={
              <InputAdornment position="end">
                <CopyToClipboard text={moodleLink} onCopy={() => setSnackOpen(true)}>
                  <IconButton aria-label={messages['share-copy']} title={messages['share-copy']} >
                    <CopyIcon />
                  </IconButton>
                </CopyToClipboard>
              </InputAdornment>
            }
          />
        </Paper>
      }
      {embed && embedCode && embedBox &&
        <Paper className={classes['embedBox']} elevation={2}>
          <label htmlFor="embedLink">{messages['share-embed-label']}</label>
          <Input
            type="text"
            fullWidth
            id="embedLink"
            value={embedCode}
            inputProps={{
              readOnly: true,
            }}
            endAdornment={
              <InputAdornment position="end">
                <CopyToClipboard text={embedCode} onCopy={() => setSnackOpen(true)}>
                  <IconButton aria-label={messages['share-copy']} title={messages['share-copy']}>
                    <CopyIcon />
                  </IconButton>
                </CopyToClipboard>
              </InputAdornment>
            }
          />
          <RadioGroup className={classes['radioGroup']} aria-label={messages['share-embed-size']} value={embedSize} onChange={handleChangeEmbedSize}>
            <FormControlLabel value={'640x390'} control={<Radio />} label="640x390" />
            <FormControlLabel value={'800x600'} control={<Radio />} label="800x600" />
            <FormControlLabel value={'100%x800'} control={<Radio />} label="100%" />
          </RadioGroup>
        </Paper>
      }
      <Snackbar
        style={{ position: 'relative' }}
        open={snackOpen}
        autoHideDuration={4000}
        onClose={handleSnackClose}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
        message={messages['share-copied']}
        action={
          <IconButton size="small" aria-label={messages['close']} title={messages['close']} color="inherit" onClick={handleSnackClose}>
            <CloseIcon fontSize="small" />
          </IconButton>
        }
      />
    </div>
  );

}