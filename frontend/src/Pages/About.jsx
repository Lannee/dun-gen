import { Button } from 'primereact/button';

export default function About() {
	return (
		<div className="about mx-3 my-5">
			<a href='https://github.com/rsp243'>
				<Button label="Github" icon="pi pi-github" />
			</a>
			<a className="mx-3" href='https://t.me/rsp243'>
				<Button label="Telegram" icon="pi pi-telegram" />
			</a>
		</div>
	);
}
