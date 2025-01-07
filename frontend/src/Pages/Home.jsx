import "./src/js/watches"
import "./src/css/Home.css"

export default function Home() {
	return (
		<div className="home">
			<div className="center-container">
				<div id="clock" className="light">
					<div className="display">
						<div className="weekdays"></div>
						<div className="alarm"></div>
						<div className="digits">
							<div className="am_pm"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	);
}
